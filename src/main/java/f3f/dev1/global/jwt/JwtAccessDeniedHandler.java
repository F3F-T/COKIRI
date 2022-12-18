package f3f.dev1.global.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import f3f.dev1.global.error.exception.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static org.springframework.http.HttpStatus.FORBIDDEN;

@Component
@Slf4j
public class JwtAccessDeniedHandler implements AccessDeniedHandler {

    private final ObjectMapper objectMapper = new ObjectMapper();
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        sendResponse(response, accessDeniedException);

    }

    private void sendResponse(HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException {
        String result = objectMapper.writeValueAsString(new ErrorResponse(FORBIDDEN, accessDeniedException.getMessage()));


        response.setContentType("application/json");
        response.setCharacterEncoding("utf-8");
        response.getWriter().write(result);
        response.setStatus(response.SC_FORBIDDEN);
    }
}
