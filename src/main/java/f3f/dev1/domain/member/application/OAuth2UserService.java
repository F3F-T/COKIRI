package f3f.dev1.domain.member.application;

import f3f.dev1.domain.member.component.GoogleAuth;
import f3f.dev1.domain.member.dao.MemberRepository;
import f3f.dev1.domain.member.dto.MemberDTO.UserLoginDto;
import f3f.dev1.domain.member.dto.OAuthDTO;
import f3f.dev1.domain.member.dto.OAuthDTO.GoogleOAuthToken;
import f3f.dev1.domain.member.dto.OAuthDTO.SocialLoginUrlDto;
import f3f.dev1.domain.member.exception.UnknownLoginException;
import f3f.dev1.domain.member.model.Member;
import f3f.dev1.domain.member.model.UserLoginType;
import f3f.dev1.global.common.constants.OAuthConstants;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Optional;

import static f3f.dev1.domain.member.dto.MemberDTO.LoginRequest;
import static f3f.dev1.domain.member.dto.MemberDTO.SignUpRequest;

@RequiredArgsConstructor
@Service
public class OAuth2UserService {
    private final GoogleAuth googleAuth;

    private final AuthService authService;

    private final MemberRepository memberRepository;



    public SocialLoginUrlDto request(String loginType) {
        String redirectUrl;
        switch (loginType) {
            case OAuthConstants.GOOGLE:{
                redirectUrl = googleAuth.getOAuthRedirectURL();
            }break;
            default:{
                throw new UnknownLoginException();
            }

        }
        return OAuthDTO.SocialLoginUrlDto.builder().url(redirectUrl).build();
    }

    public UserLoginDto oAuthLogin(String loginType, String code) throws IOException {

        switch (loginType) {
            case OAuthConstants.GOOGLE: {
                // 구글로 일회성 코드를 보내 액세스 토큰이 담긴 응답 객체를 받아옴
                ResponseEntity<String> accessTokenResponse = googleAuth.requestAccessToken(code);
                // 응답객체가 JSON 형식으로 되어 있으니, 이를 역직렬화해서 자바 객체에 담음
                GoogleOAuthToken oAuthToken = googleAuth.getAccessToken(accessTokenResponse);
                // 액세스 토큰을 다시 구글로 보내 사용자 정보가 담긴 응답 객체를 받아옴
                ResponseEntity<String> userInfoResponse = googleAuth.requestUserInfo(oAuthToken);
                // 다시 Json 형식의 응답 객체를 자바 객체로 역 직렬화
                OAuthDTO.GoogleUser googleUser = googleAuth.getUserInfo(userInfoResponse);
                Optional<Member> byEmail = memberRepository.findByEmail(googleUser.getEmail());
                // 이메일로 디비에 유저 존재
                if (byEmail.isPresent()) {
                    Member member = byEmail.get();
                    // 구글 로그인으로 생성된 유저
                    if (member.getUserLoginType() != UserLoginType.GOOGLE) {

                        throw new IllegalArgumentException("디비 이메일 중복");
                    }

                } else {
                    SignUpRequest signUpRequest = SignUpRequest.builder()
                            .email(googleUser.getEmail())
                            .userName(googleUser.getName())
                            .userLoginType(UserLoginType.GOOGLE)
                            .password(System.getenv("GOOGLE_USER_PWD")).build();
                    authService.signUp(signUpRequest);

                }
                return authService.login(LoginRequest.builder().email(googleUser.getEmail()).password(System.getenv("GOOGLE_USER_PWD")).build());

            }
            default: {
                throw new UnknownLoginException();
            }
        }

    }
}