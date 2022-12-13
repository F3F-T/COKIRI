package f3f.dev1.domain.user.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class UserErrorResponse {
    public static final ResponseEntity<String> DUPLICATE_EMAIL = new ResponseEntity<>("이메일 중복", HttpStatus.CONFLICT);

    public static final ResponseEntity<String> DUPLICATE_NICKNAME = new ResponseEntity<>("닉네임 중복", HttpStatus.CONFLICT);

    public static final ResponseEntity<String> DUPLICATE_PHONE_NUMBER = new ResponseEntity<>("전화번호 중복", HttpStatus.CONFLICT);

    public static final ResponseEntity<String> USER_NOT_FOUND =
            new ResponseEntity<>(
                    "ID 또는 PW를 확인하세요.", HttpStatus.NOT_FOUND
            );

    public static final ResponseEntity<String> UNAUTHENTICATED = new ResponseEntity<>("로그인 후에 사용하세요.", HttpStatus.UNAUTHORIZED);
}
