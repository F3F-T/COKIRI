package f3f.dev1.domain.member.application;

import f3f.dev1.domain.member.dao.MemberRepository;
import f3f.dev1.domain.member.model.Member;
import f3f.dev1.domain.scrap.application.ScrapService;
import f3f.dev1.domain.scrap.dao.ScrapRepository;
import f3f.dev1.domain.scrap.dto.ScrapDTO;
import f3f.dev1.domain.scrap.exception.UserScrapNotFoundException;
import f3f.dev1.domain.scrap.model.Scrap;
import f3f.dev1.domain.token.dto.TokenDTO.AccessTokenDTO;
import f3f.dev1.domain.token.dto.TokenDTO.TokenIssueDTO;
import f3f.dev1.domain.token.exception.ExpireRefreshTokenException;
import f3f.dev1.domain.token.exception.InvalidRefreshTokenException;
import f3f.dev1.domain.token.exception.LogoutUserException;
import f3f.dev1.domain.token.exception.TokenNotMatchException;
import f3f.dev1.global.error.exception.NotFoundByIdException;
import f3f.dev1.global.jwt.JwtTokenProvider;
import f3f.dev1.global.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import static f3f.dev1.domain.member.dto.MemberDTO.*;
import static f3f.dev1.domain.token.dto.TokenDTO.TokenInfoDTO;
import static f3f.dev1.global.common.constants.JwtConstants.REFRESH_TOKEN_EXPIRE_TIME;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    private final ScrapRepository scrapRepository;

    private final ScrapService scrapService;

    private final RedisTemplate<String, String> redisTemplate;



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
    public UserLoginDto login(LoginRequest loginRequest) {
        // 1. 이메일, 비밀번호 기반으로 토큰 생성
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = loginRequest.toAuthentication();
        // 2. 실제로 검증이 이뤄지는 부분,
        // authenticate 메서드가 실행이 될 때 CustomUserDetailsService 에서 만들었던 loadUserByUsername 메서드가 실행됨
        Authentication authenticate = authenticationManagerBuilder.getObject().authenticate(usernamePasswordAuthenticationToken);

        // 3. 인증 정보를 기반으로 jwt 토큰 생성
        TokenInfoDTO tokenInfoDTO = jwtTokenProvider.generateTokenDto(authenticate);
        // 4. refesh token 저장
        ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
        valueOperations.set(authenticate.getName(), tokenInfoDTO.getRefreshToken());
        valueOperations.set(tokenInfoDTO.getAccessToken(), tokenInfoDTO.getRefreshToken());
        redisTemplate.expire(authenticate.getName(), REFRESH_TOKEN_EXPIRE_TIME, TimeUnit.MILLISECONDS);
        redisTemplate.expire(tokenInfoDTO.getAccessToken(), REFRESH_TOKEN_EXPIRE_TIME, TimeUnit.MILLISECONDS);
        // 5. 토큰 발급

        Member member = memberRepository.findById(Long.parseLong(authenticate.getName())).orElseThrow(NotFoundByIdException::new);
        Scrap scrap = scrapRepository.findScrapByMemberId(member.getId()).orElseThrow(NotFoundByIdException::new);


        return UserLoginDto.builder().userInfo(member.toUserInfo(scrap.getId())).tokenInfo(tokenInfoDTO.toTokenIssueDTO()).build();
    }

    @Transactional
    public TokenIssueDTO reissue(AccessTokenDTO accessTokenDTO) {
        ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
        String accessByRefresh = valueOperations.get(accessTokenDTO.getAccessToken());
        if (accessByRefresh == null) {
            throw new ExpireRefreshTokenException();
        }
        // 1. refresh token 검증
        if (!jwtTokenProvider.validateToken(accessByRefresh)) {
            throw new InvalidRefreshTokenException();
        }

        // 2. Access Token에서 멤버 아이디 가져오기
        Authentication authentication = jwtTokenProvider.getAuthentication(accessTokenDTO.getAccessToken());

        // 3. 저장소에서 member id를 기반으로 refresh token 값 가져옴
        String accessByMemberId = valueOperations.get(authentication.getName());
        if (accessByMemberId == null) {
            throw new LogoutUserException();
        }

        // 4. refresh token이 일치하는지 검사,

        if (!accessByMemberId.equals(accessByRefresh)) {
            throw new TokenNotMatchException();
        }

        // 5. 새로운 토큰 생성
        TokenInfoDTO tokenInfoDTO = jwtTokenProvider.generateTokenDto(authentication);
        // 6. 저장소 정보 업데이트
        valueOperations.set(authentication.getName(), tokenInfoDTO.getRefreshToken());
        valueOperations.set(tokenInfoDTO.getAccessToken(), tokenInfoDTO.getRefreshToken());
        redisTemplate.expire(authentication.getName(), REFRESH_TOKEN_EXPIRE_TIME, TimeUnit.MILLISECONDS);
        redisTemplate.expire(tokenInfoDTO.getAccessToken(), REFRESH_TOKEN_EXPIRE_TIME, TimeUnit.MILLISECONDS);


        // 토큰 발급
        return tokenInfoDTO.toTokenIssueDTO();
    }

    @Transactional
    public String logout(String token) throws IOException {
        ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
        valueOperations.getAndDelete(Long.toString(SecurityUtil.getCurrentMemberId()));
        valueOperations.getAndDelete(token);

        return "SUCCESS";
    }



}
