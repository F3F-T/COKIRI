package f3f.dev1.domain.token.exception.handler;

import f3f.dev1.domain.token.exception.ExpireRefreshTokenException;
import f3f.dev1.domain.token.exception.response.TokenErrorResponse;
import f3f.dev1.global.error.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import static f3f.dev1.domain.token.exception.response.TokenErrorResponse.*;

@Slf4j
@RestControllerAdvice
public class TokenExceptionHandler {
    @ExceptionHandler(ExpireRefreshTokenException.class)
    protected final ResponseEntity<ErrorResponse> handleExpireRefresh(
            ExpireRefreshTokenException ex, WebRequest request) {
        log.debug("만료된 리프레쉬");
        return EXPIRE_REFRESH_TOKEN;
    }
}
