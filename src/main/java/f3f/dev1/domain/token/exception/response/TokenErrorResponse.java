package f3f.dev1.domain.token.exception.response;

import f3f.dev1.global.error.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class TokenErrorResponse {
    public static final ResponseEntity<ErrorResponse> EXPIRE_REFRESH_TOKEN = new ResponseEntity<>(ErrorResponse.builder().status(HttpStatus.UNAUTHORIZED).message("EXPIRED_REFRESH_TOKEN").build(), HttpStatus.UNAUTHORIZED);

}
