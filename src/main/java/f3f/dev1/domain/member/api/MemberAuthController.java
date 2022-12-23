package f3f.dev1.domain.member.api;

import f3f.dev1.domain.member.application.AuthService;
import f3f.dev1.domain.member.application.EmailCertificationService;
import f3f.dev1.domain.member.application.MemberService;
import f3f.dev1.domain.token.dto.TokenDTO;
import f3f.dev1.domain.token.dto.TokenDTO.TokenInfoDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

import static f3f.dev1.domain.member.dto.MemberDTO.*;
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
    public ResponseEntity<EmailConfirmCodeDto> mailConfirm(@RequestBody ConfirmEmailDto confirmEmailDto) throws Exception {

        String code = emailCertificationService.sendSimpleMessage(confirmEmailDto.getEmail());
        EmailConfirmCodeDto codeDto = EmailConfirmCodeDto.builder().code(code).build();
        return ResponseEntity.ok(codeDto);
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
    public ResponseEntity<UserLoginDto> login(@RequestBody LoginRequest loginRequest, HttpServletResponse response) {
        return ResponseEntity.ok(authService.login(loginRequest, response));
    }

    // 재발급
    @PostMapping(value = "/reissue")
    public ResponseEntity<TokenInfoDTO> reissue(@RequestBody TokenDTO.TokenIssueDTO tokenReissueDTO, HttpServletResponse response, @CookieValue(name = REFRESH_TOKEN) String refreshToken) {
        return ResponseEntity.ok(authService.reissue(tokenReissueDTO, response, refreshToken));
    }



}
