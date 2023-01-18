package f3f.dev1.domain.address.exception.handler;


import f3f.dev1.domain.address.exception.InvalidDeleteRequest;
import f3f.dev1.domain.address.exception.WrongUserException;
import f3f.dev1.domain.address.exception.response.AddressErrorResponse;
import f3f.dev1.global.error.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import static f3f.dev1.domain.address.exception.response.AddressErrorResponse.INVALID_ADDRESS_DELETE_REQUEST;
import static f3f.dev1.domain.address.exception.response.AddressErrorResponse.WRONG_USER;

@Slf4j
@RestControllerAdvice
public class AddressExceptionHandler {

    @ExceptionHandler(WrongUserException.class)
    protected final ResponseEntity<ErrorResponse> handleWrongUser(
            WrongUserException ex, WebRequest request
    ) {
        log.info("잘못된 유저와 주소");
        return WRONG_USER;
    }

    @ExceptionHandler(InvalidDeleteRequest.class)
    protected final ResponseEntity<ErrorResponse> handleInvalidDeleteRequest(
            InvalidDeleteRequest ex, WebRequest request
    ) {
        log.info("이건 잘못됨");
        return INVALID_ADDRESS_DELETE_REQUEST;
    }
}
