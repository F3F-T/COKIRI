package f3f.dev1.domain.address.exception.response;

import f3f.dev1.global.error.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class AddressErrorResponse {

    public static final ResponseEntity<ErrorResponse> WRONG_USER = new ResponseEntity<>(ErrorResponse.builder().status(HttpStatus.CONFLICT).message("잘못된 유저와 주소입니다.").build(), HttpStatus.CONFLICT);
}
