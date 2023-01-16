package f3f.dev1.domain.member.api;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import f3f.dev1.domain.member.application.AuthService;
import f3f.dev1.domain.member.application.EmailCertificationService;
import f3f.dev1.domain.member.application.MemberService;
import f3f.dev1.domain.member.application.OAuth2UserService;
import f3f.dev1.domain.member.dto.OAuthDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.mail.Multipart;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static f3f.dev1.domain.member.dto.MemberDTO.*;
import static f3f.dev1.domain.member.dto.OAuthDTO.*;
import static f3f.dev1.domain.token.dto.TokenDTO.AccessTokenDTO;
import static f3f.dev1.domain.token.dto.TokenDTO.TokenIssueDTO;

@Slf4j
@RestController
@RequiredArgsConstructor
public class MemberAuthController {
    private final String S3Bucket = "cokiri-image/image/profileImage";
    private final MemberService memberService;

    private final EmailCertificationService emailCertificationService;

    private final AuthService authService;

    private final OAuth2UserService oAuth2UserService;

    private final AmazonS3Client amazonS3Client;

    // 이미지 테스트 업로드
    @PostMapping(value = "/auth/image")
    public ResponseEntity<ImageUrlDto> upload(MultipartFile[] imageFiles) throws IOException {
        List<String> imagePathList = new ArrayList<>();

        for(MultipartFile multipartFile: imageFiles) {
            String originalName = multipartFile.getOriginalFilename(); // 파일 이름
            long size = multipartFile.getSize(); // 파일 크기

            ObjectMetadata objectMetaData = new ObjectMetadata();
            objectMetaData.setContentType(multipartFile.getContentType());
            objectMetaData.setContentLength(size);

            // S3에 업로드
            amazonS3Client.putObject(
                    new PutObjectRequest(S3Bucket, originalName, multipartFile.getInputStream(), objectMetaData)
                            .withCannedAcl(CannedAccessControlList.PublicRead)
            );

            String imagePath = amazonS3Client.getUrl(S3Bucket, originalName).toString(); // 접근가능한 URL 가져오기
            imagePathList.add(imagePath);
        }

        return ResponseEntity.ok(ImageUrlDto.builder().imageUrls(imagePathList).build());
    }


    // 이메일 중복 확인
    @PostMapping(value = "/auth/check-email")
    public ResponseEntity<RedunCheckDto> emailDuplicateCheck(@RequestBody CheckEmailDto checkEmailDto) {
        return ResponseEntity.ok(memberService.existsByEmail(checkEmailDto.getEmail()));
    }

    // 닉네임 중복 확인
    @PostMapping(value = "/auth/check-nickname")
    public ResponseEntity<RedunCheckDto> nicknameDuplicateCheck(@RequestBody CheckNicknameDto checkNicknameDto) {
        return ResponseEntity.ok(memberService.existsByNickname(checkNicknameDto.getNickname()));
    }

    // 전화번호 중복 확인
    @PostMapping(value = "/auth/check-phone")
    public ResponseEntity<RedunCheckDto> phoneNumberDuplicateCheck(@RequestBody CheckPhoneNumberDto checkPhoneNumberDto) {
        return ResponseEntity.ok(memberService.existsByPhoneNumber(checkPhoneNumberDto.getPhoneNumber()));

    }

    // 이메일 인증 요청
    @PostMapping(value = "/auth/mailConfirm")
    public ResponseEntity<EmailSentDto> mailConfirm(@RequestBody ConfirmEmailDto confirmEmailDto) throws Exception {

        emailCertificationService.sendSimpleMessage(confirmEmailDto.getEmail());
        return ResponseEntity.ok(EmailSentDto.builder().email(confirmEmailDto.getEmail()).success(true).build());
    }

    // 코드 인증 요청
    @PostMapping(value = "/auth/codeConfirm")
    public ResponseEntity<CodeConfirmDto> codeConfirm(@RequestBody EmailConfirmCodeDto emailConfirmCodeDto) {
        return ResponseEntity.ok(emailCertificationService.confirmCode(emailConfirmCodeDto));

    }

    // 이메일 찾기
    // TODO: 던지는 예외 메시지도 한번 다듬어야될 것 같음, 같은 예외 재사용이 많아서 비밀번호 찾기에서 유저 못 찾았는데 ID 비밀번호 확인하라는 메시지가 출력됨
    @PostMapping(value = "/auth/find/email")
    public ResponseEntity<EncryptEmailDto> findEmail(@RequestBody FindEmailDto findEmailDto) {
        EncryptEmailDto userEmail = memberService.findUserEmail(findEmailDto);
        return new ResponseEntity<>(userEmail, HttpStatus.OK);
    }

    // 비밀번호 찾기
    @PostMapping(value = "/auth/find/password")
    public ResponseEntity<ReturnPasswordDto> findPassword(@RequestBody FindPasswordDto findPasswordDto) {
        ReturnPasswordDto userPassword = memberService.findUserPassword(findPasswordDto);
        return new ResponseEntity<>(userPassword, HttpStatus.OK);

    }

    // 회원가입
    @PostMapping(value = "/auth/signup")
    public ResponseEntity<String> signUp(@RequestBody SignUpRequest signUpRequest) {
        // TODO: 회원가입할때 정보 검증을 프론트에서 할지 백에서할지 결정해야함


        return new ResponseEntity<>(authService.signUp(signUpRequest), HttpStatus.CREATED);
    }

    // 로그인 - TODO 크롬 자동 로그인 이용하려면 form data 형식도 열어놔야할듯
    @PostMapping(value = "/login", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserLoginDto> loginJson(@RequestBody LoginRequest loginRequest) {
        log.info("로그인 호출됐음");
        log.info("json 형식으로 데이터 넘어옴");
        log.info("email " + loginRequest.getEmail());
        log.info("pwd " + loginRequest.getPassword());
        return ResponseEntity.ok(authService.login(loginRequest));
    }

    @PostMapping(value = "/login", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseEntity<UserLoginDto> loginForm(LoginRequest loginRequest) {
        log.info("로그인 호출됐음");
        log.info("form 형식으로 데이터 넘어옴");
        log.info("email " + loginRequest.getEmail());
        log.info("pwd " + loginRequest.getPassword());
        return ResponseEntity.ok(authService.login(loginRequest));
    }


    // 로그아웃 리다이렉트 페이지
    @GetMapping(value = "/logout-redirect")
    public ResponseEntity<String> loginRedirect(){
        return ResponseEntity.ok("LOGOUT");
    }

    // 로그아웃은 스프링 시큐리티에서 기본적으로 제공해주는 기능사용


    @PostMapping(value = "/auth/google_login")
    public ResponseEntity<UserLoginDto> googleLogin(@RequestBody GoogleLoginRequest googleLoginRequest) {
        return ResponseEntity.ok(oAuth2UserService.googleLogin(googleLoginRequest));
    }

    // 외부 API 로그인 요청
    @GetMapping(value = "/auth/social_login/{loginType}")
    public ResponseEntity<SocialLoginUrlDto> socialLogin(@PathVariable(name = "loginType") String loginType) {
        return ResponseEntity.ok(oAuth2UserService.request(loginType.toUpperCase()));

    }

    // 구글 로그인 콜백 처리
    @GetMapping(value = "/auth/social_login/{loginType}/callback")
    public ResponseEntity<UserLoginDto> callback(@PathVariable(name = "loginType") String loginType, @RequestParam(name = "code") String code) throws IOException {
        log.debug("code " + code);
        System.out.println("code = " + code);

        UserLoginDto userLoginDto = oAuth2UserService.oAuthLogin(loginType.toUpperCase(), code);
        if (userLoginDto.getUserInfo().getNickname() == null) {
            return new ResponseEntity<>(userLoginDto, HttpStatus.CREATED);
        }
        return ResponseEntity.ok(userLoginDto);
    }



    // 재발급
    @PostMapping(value = "/auth/reissue")
    public ResponseEntity<TokenIssueDTO> reissue(@RequestBody AccessTokenDTO accessTokenDTO) {
        return ResponseEntity.ok(authService.reissue(accessTokenDTO));
    }



}
