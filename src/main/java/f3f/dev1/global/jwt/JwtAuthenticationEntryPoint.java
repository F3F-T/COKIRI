package f3f.dev1.global.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import f3f.dev1.global.error.ErrorResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
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
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final ObjectMapper objectMapper;
    private final RedisTemplate<String, String> redisTemplate;

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        sendResponse(response, authException);

    }

    private void sendResponse(HttpServletResponse response, AuthenticationException authException) throws IOException {

        String result = objectMapper.writeValueAsString(new ErrorResponse(UNAUTHORIZED, "EXPIRED_ACCESS_TOKEN"));


        response.setContentType("application/json");
        response.setCharacterEncoding("utf-8");
        response.getWriter().write(result);
        response.setStatus(response.SC_UNAUTHORIZED);
    }


}
