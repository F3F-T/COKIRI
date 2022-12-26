package f3f.dev1.domain.member.api;

import f3f.dev1.domain.member.application.AuthService;
import f3f.dev1.domain.member.application.EmailCertificationService;
import f3f.dev1.domain.member.application.MemberService;
import f3f.dev1.domain.token.dto.TokenDTO;
import f3f.dev1.domain.token.dto.TokenDTO.TokenInfoDTO;
import f3f.dev1.global.common.constants.GoogleAuthConstants;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static f3f.dev1.domain.member.dto.MemberDTO.*;
import static f3f.dev1.domain.token.dto.TokenDTO.*;
import static f3f.dev1.global.common.constants.GoogleAuthConstants.GOOGLE_LOGIN_URL;
import static f3f.dev1.global.common.constants.GoogleAuthConstants.GOOGLE_REDIRECT_URL;
import static f3f.dev1.global.common.constants.JwtConstants.REFRESH_TOKEN;

@Slf4j
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class MemberAuthController {
    private final MemberService memberService;

    private final EmailCertificationService emailCertificationService;

    private final AuthService authService;


    // 이메일 중복 확인
    @PostMapping(value = "/check-email")
    public ResponseEntity<RedunCheckDto> emailDuplicateCheck(@RequestBody CheckEmailDto checkEmailDto) {
        return ResponseEntity.ok(memberService.existsByEmail(checkEmailDto.getEmail()));
    }

    // 닉네임 중복 확인
    @PostMapping(value = "/check-nickname")
    public ResponseEntity<RedunCheckDto> nicknameDuplicateCheck(@RequestBody CheckNicknameDto checkNicknameDto) {
        return ResponseEntity.ok(memberService.existsByNickname(checkNicknameDto.getNickname()));
    }

    // 전화번호 중복 확인
    @PostMapping(value = "/check-phone")
    public ResponseEntity<RedunCheckDto> phoneNumberDuplicateCheck(@RequestBody CheckPhoneNumberDto checkPhoneNumberDto) {
        return ResponseEntity.ok(memberService.existsByPhoneNumber(checkPhoneNumberDto.getPhoneNumber()));

    }

    // 이메일 인증 요청
    @PostMapping(value = "/mailConfirm")
    public ResponseEntity<EmailSentDto> mailConfirm(@RequestBody ConfirmEmailDto confirmEmailDto) throws Exception {

        emailCertificationService.sendSimpleMessage(confirmEmailDto.getEmail());
        return ResponseEntity.ok(EmailSentDto.builder().email(confirmEmailDto.getEmail()).success(true).build());
    }

    // 코드 인증 요청
    @PostMapping(value = "/codeConfirm")
    public ResponseEntity<CodeConfirmDto> codeConfirm(@RequestBody EmailConfirmCodeDto emailConfirmCodeDto) {
        return ResponseEntity.ok(emailCertificationService.confirmCode(emailConfirmCodeDto));

    }

    // 이메일 찾기
    // TODO: 던지는 예외 메시지도 한번 다듬어야될 것 같음, 같은 예외 재사용이 많아서 비밀번호 찾기에서 유저 못 찾았는데 ID 비밀번호 확인하라는 메시지가 출력됨
    @PostMapping(value = "/find/email")
    public ResponseEntity<EncryptEmailDto> findEmail(@RequestBody FindEmailDto findEmailDto) {
        EncryptEmailDto userEmail = memberService.findUserEmail(findEmailDto);
        return new ResponseEntity<>(userEmail, HttpStatus.OK);
    }

    // 비밀번호 찾기
    @PostMapping(value = "/find/password")
    public ResponseEntity<ReturnPasswordDto> findPassword(@RequestBody FindPasswordDto findPasswordDto) {
        ReturnPasswordDto userPassword = memberService.findUserPassword(findPasswordDto);
        return new ResponseEntity<>(userPassword, HttpStatus.OK);

    }

    // 회원가입
    @PostMapping(value = "/signup")
    public ResponseEntity<String> signUp(@RequestBody SignUpRequest signUpRequest) {
        // TODO: 회원가입할때 정보 검증을 프론트에서 할지 백에서할지 결정해야함


        return new ResponseEntity<>(authService.signUp(signUpRequest), HttpStatus.CREATED);
    }

    // 로그인
    @PostMapping(value = "/login")
    public ResponseEntity<UserLoginDto> login(@RequestBody LoginRequest loginRequest) {
        return ResponseEntity.ok(authService.login(loginRequest));
    }

//    // 구글 로그인 페이지로 이동
//    @GetMapping(value = "/google-login")
//    public ResponseEntity<GoogleLoginUrlDto> googleLoginUrl() {
//        return ResponseEntity.ok(GoogleLoginUrlDto.builder().url(GOOGLE_LOGIN_URL + "/o/oauth2/v2/auth?client_id=" + System.getenv("GOOGLE_CLIENT") + "&redirect_url=" + GOOGLE_REDIRECT_URL + "&response_type=code&scope=email%20profile%20openid&access_type=offline").build());
//    }
//
//    // 구글 연동 정보 조회
//    @PostMapping(value = "/google-login")
//    public ResponseEntity<UserLoginDto> googleLogin(HttpServletRequest request, @RequestParam(value = "code") String authCode, HttpServletResponse response) {
//        // restTemplate 호출
//        RestTemplate restTemplate = new RestTemplate();
//
//        GoogleOAuthRequest googleOAuthRequest = GoogleOAuthRequest.builder()
//                .clientId(System.getenv("GOOGLE_CLIENT"))
//                .clientSecret(System.getenv("GOOGLE_SECRET"))
//                .code(authCode)
//                .redirectUrl(GOOGLE_REDIRECT_URL)
//                .grantType("authorization_code").build();
//
//        ResponseEntity<JSON>
//    }

    // 재발급
    @PostMapping(value = "/reissue")
    public ResponseEntity<TokenIssueDTO> reissue(@RequestBody AccessTokenDTO accessTokenDTO) {
        return ResponseEntity.ok(authService.reissue(accessTokenDTO));
    }



}
