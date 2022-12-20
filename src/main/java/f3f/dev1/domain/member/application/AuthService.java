package f3f.dev1.domain.member.application;

import f3f.dev1.domain.member.dao.MemberRepository;
import f3f.dev1.domain.member.model.Member;
import f3f.dev1.domain.scrap.application.ScrapService;
import f3f.dev1.domain.scrap.dto.ScrapDTO;
import f3f.dev1.domain.token.dao.RefreshTokenRepository;
import f3f.dev1.domain.token.exception.InvalidRefreshTokenException;
import f3f.dev1.domain.token.exception.LogoutUserException;
import f3f.dev1.domain.token.model.RefreshToken;
import f3f.dev1.global.common.constants.SecurityConstants;
import f3f.dev1.global.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseCookie;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;
import java.io.IOException;

import static f3f.dev1.domain.member.dto.MemberDTO.LoginRequest;
import static f3f.dev1.domain.member.dto.MemberDTO.SignUpRequest;
import static f3f.dev1.domain.token.dto.TokenDTO.TokenInfoDTO;
import static f3f.dev1.domain.token.dto.TokenDTO.TokenRequestDTO;
import static f3f.dev1.global.common.constants.JwtConstants.*;
import static f3f.dev1.global.common.constants.SecurityConstants.*;
import static f3f.dev1.global.common.constants.SecurityConstants.JSESSIONID;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final RefreshTokenRepository refreshTokenRepository;

    private final ScrapService scrapService;

    private final HttpSession session;

    @Transactional
    public String signUp(SignUpRequest signUpRequest) {
        signUpRequest.encrypt(passwordEncoder);

        Member member = signUpRequest.toEntity();

        memberRepository.save(member);
        ScrapDTO.CreateScrapDTO userScrap = ScrapDTO.CreateScrapDTO.builder().user(member).build();
        scrapService.createScrap(userScrap);
        return "CREATED";
    }

    @Transactional
    public TokenInfoDTO login(LoginRequest loginRequest, HttpServletResponse response) {
        // 1. 이메일, 비밀번호 기반으로 토큰 생성
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = loginRequest.toAuthentication();
        // 2. 실제로 검증이 이뤄지는 부분,
        Authentication authenticate = authenticationManagerBuilder.getObject().authenticate(usernamePasswordAuthenticationToken);

        // 3. 인증 정보를 기반으로 jwt 토큰 생성
        TokenInfoDTO tokenInfoDTO = jwtTokenProvider.generateTokenDto(authenticate);
        // 4. refesh token 저장
//        RefreshToken refreshToken = RefreshToken.builder()
//                .key(authenticate.getName())
//                .value(tokenInfoDTO.getRefreshToken())
//                .build();
//
//        refreshTokenRepository.save(refreshToken);


        String refreshToken = tokenInfoDTO.getRefreshToken();
        saveRefreshTokenInStorage(refreshToken); // 추후 DB 나 어딘가 저장 예정
        setRefreshTokenInCookie(response, refreshToken); // 리프레시 토큰 쿠키에 저장
        // 5. 토큰 발급
        return tokenInfoDTO;
    }

    @Transactional
    public TokenInfoDTO reissue(TokenRequestDTO tokenRequestDTO,HttpServletResponse response) {
        // 1. refresh token 검증
        if (!jwtTokenProvider.validateToken(tokenRequestDTO.getRefreshToken())) {
            throw new InvalidRefreshTokenException();
        }

        // 2. Access Token에서 멤버 아이디 가져오기
        Authentication authentication = jwtTokenProvider.getAuthentication(tokenRequestDTO.getAccessToken());

        // 3. 저장소에서 member id를 기반으로 refresh token 값 가져옴
//        RefreshToken refreshToken = refreshTokenRepository.findByKey(authentication.getName()).orElseThrow(LogoutUserException::new);
        String refreshToken = (String)session.getAttribute(REFRESH_TOKEN);
        if(refreshToken == null){
            throw new LogoutUserException();
        }

        // 4. refresh token이 일치하는지 검사,
        if (!refreshToken.equals(tokenRequestDTO.getRefreshToken())) {
            throw new IllegalArgumentException("토큰의 유저 정보가 일치하지 않습니다");
        }

        // 5. 새로운 토큰 생성
        TokenInfoDTO tokenInfoDTO = jwtTokenProvider.generateTokenDto(authentication);
        // 6. 저장소 정보 업데이트
//        RefreshToken newRefreshToken = refreshToken.updateValue(tokenInfoDTO.getRefreshToken());
//        refreshTokenRepository.save(newRefreshToken);
        saveRefreshTokenInStorage(tokenInfoDTO.getRefreshToken());// 추후 디비에 저장
        setRefreshTokenInCookie(response,tokenInfoDTO.getRefreshToken()); // 쿠키에 refresh 토큰 저장

        // 토큰 발급
        return tokenInfoDTO;
    }

    @Transactional
    public void logout(HttpServletRequest request, HttpServletResponse response) throws IOException {

        request.getSession().invalidate();

        deleteCookie(response,JSESSIONID);
        deleteCookie(response, REMEMBER_ME);
        deleteCookie(response,REFRESH_TOKEN);

//        response.sendRedirect("/login"); //로그아웃 시 로그인 할 수 있는 페이지로 이동하도록 처리한다.
    }

    /**
     * 쿠키에 refresh 토큰 저장
     */
    private void setRefreshTokenInCookie(HttpServletResponse response, String refreshToken) {
        ResponseCookie cookie = ResponseCookie.from(REFRESH_TOKEN, refreshToken)
                .maxAge(REFRESH_TOKEN_COOKIE_EXPIRE_TIME) //7일
                .path("/")
                .secure(true)
                .sameSite("None")
                .httpOnly(true)
                .build();
        response.setHeader(SET_COOKIE, cookie.toString());
    }

    /**
     * 저장소에 토큰 저장 추후에 DB 나 캐시 고려
     */
    private void saveRefreshTokenInStorage(String refreshToken) {

        session.setAttribute(REFRESH_TOKEN, refreshToken);
    }


    /**
     * 쿠키 제거
     */
    private void deleteCookie(HttpServletResponse response,String cookieName) {
        Cookie cookie = new Cookie(cookieName, null); // choiceCookieName(쿠키 이름)에 대한 값을 null로 지정
        cookie.setMaxAge(0); // 유효시간을 0으로 설정
        response.addCookie(cookie);
    }


}
