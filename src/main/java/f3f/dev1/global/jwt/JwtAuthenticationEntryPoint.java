package f3f.dev1.global.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import f3f.dev1.global.error.exception.ErrorResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static org.springframework.http.HttpStatus.UNAUTHORIZED;

@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final ObjectMapper objectMapper = new ObjectMapper();
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        sendResponse(response, authException);

    }

    private void sendResponse(HttpServletResponse response, AuthenticationException authException) throws IOException {

        String result = objectMapper.writeValueAsString(new ErrorResponse(UNAUTHORIZED, authException.getMessage()));


        response.setContentType("application/json");
        response.setCharacterEncoding("utf-8");
        response.getWriter().write(result);
        response.setStatus(response.SC_UNAUTHORIZED);
    }
}
