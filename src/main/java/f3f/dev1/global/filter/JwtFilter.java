package f3f.dev1.global.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import f3f.dev1.domain.member.exception.NotAuthorizedException;
import f3f.dev1.domain.token.exception.response.ExpireAccessTokenException;
import f3f.dev1.global.error.ErrorResponse;
import f3f.dev1.global.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static f3f.dev1.global.common.constants.JwtConstants.AUTHORIZATION_HEADER;
import static f3f.dev1.global.common.constants.JwtConstants.BEARER_PREFIX;

@RequiredArgsConstructor
@Slf4j
public class JwtFilter extends OncePerRequestFilter {
    private final JwtTokenProvider jwtTokenProvider;

    private final RedisTemplate<String, String> redisTemplate;

    private final ObjectMapper mapper;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        log.info(request.getMethod()+" "+ request.getRequestURI());

        try {
            / 1. request Header에서 토큰 꺼냄, 여기서 HTTP ONLY 쿠키에서 읽어오게 변경 가능
            String jwt = resolveToken(request);
            // 2. validateToken으로 유효성 검사
            // 정상 토큰이면, Authentication을 가져와서 SecurityContext에 저장
            if (jwt != null) {
                if (StringUtils.hasText(jwt) && jwtTokenProvider.validateToken(jwt)) {
                    Authentication authentication = jwtTokenProvider.getAuthentication(jwt);
                    User user = (User) authentication.getPrincipal();
                    if (user.getUsername() != null && redisTemplate.hasKey(user.getUsername())) {
                        log.info("memberId : " + user.getUsername());
                        SecurityContextHolder.getContext().setAuthentication(authentication);
                    }
                } else {
                    log.info("만료된 엑세스 토큰이다");
                    throw new ExpireAccessTokenException();
                }
            }
            }

            filterChain.doFilter(request, response);
        } catch (ExpireAccessTokenException e) {
            log.info(e.getMessage());
            log.info(e.getClass().getName());
            String result = mapper.writeValueAsString(new ErrorResponse(HttpStatus.UNAUTHORIZED, e.getMessage()));
            response.setContentType("application/json");
            response.setCharacterEncoding("utf-8");
            response.setStatus(response.SC_UNAUTHORIZED);
            response.setHeader("Access-Control-Allow-Origin", "https://main.d8tw528p0jeqh.amplifyapp.com");
            try {
                response.getWriter().write(result);
            } catch (IOException exception) {
                e.printStackTrace();
            }

        }  catch (Exception e) {
            log.info(e.getMessage());
            log.info(e.getClass().getName());
            e.printStackTrace();
            String result = mapper.writeValueAsString(new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage()));
            response.setContentType("application/json");
            response.setCharacterEncoding("utf-8");
            response.setStatus(response.SC_INTERNAL_SERVER_ERROR);
            response.setHeader("Access-Control-Allow-Origin", "https://main.d8tw528p0jeqh.amplifyapp.com");
            try {
                response.getWriter().write(result);
            } catch (IOException exception) {
                e.printStackTrace();
            }
        }

    }

    private String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader(AUTHORIZATION_HEADER);
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(BEARER_PREFIX)) {
            return bearerToken.substring(7);
        }
        return null;
    }

}
