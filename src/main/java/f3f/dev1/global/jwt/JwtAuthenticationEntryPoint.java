package f3f.dev1.global.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import f3f.dev1.global.error.ErrorResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static f3f.dev1.global.common.constants.JwtConstants.AUTHORIZATION_HEADER;
import static f3f.dev1.global.common.constants.JwtConstants.BEARER_PREFIX;
import static org.springframework.http.HttpStatus.CONFLICT;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final ObjectMapper objectMapper;
    private final RedisTemplate<String, String> redisTemplate;

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        log.info(String.valueOf(authException.getClass()));
        sendResponse(response, authException);

    }

    private void sendResponse(HttpServletResponse response, AuthenticationException authException) throws IOException {
        String result;
        if (authException instanceof BadCredentialsException) {
            result = objectMapper.writeValueAsString(new ErrorResponse(CONFLICT, "INVALID_EMAIL_PASSWORD"));
            response.setStatus(response.SC_CONFLICT);
        } else {
            result = objectMapper.writeValueAsString(new ErrorResponse(UNAUTHORIZED, "INVALID_ACCESS_TOKEN"));
            response.setStatus(response.SC_UNAUTHORIZED);
//            result = objectMapper.writeValueAsString(new ErrorResponse(UNAUTHORIZED, authException.getMessage()));
        }



        response.setContentType("application/json");
        response.setCharacterEncoding("utf-8");
        response.getWriter().write(result);

    }


}
