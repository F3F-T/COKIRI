package f3f.dev1.global.common.constants;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class ResponseConstants {
    public static final ResponseEntity<String> OK = ResponseEntity.ok("OK");
    public static final ResponseEntity<String> DELETE = ResponseEntity.ok("DELETE");
    public static final ResponseEntity<String> CREATE =
            new ResponseEntity<>("CREATED", HttpStatus.CREATED);
    public static final ResponseEntity<Void> FAIL = ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();

    public static final ResponseEntity<String> UPDATE = ResponseEntity.ok("UPDATE");
    public static final ResponseEntity<String> COMPLETE = ResponseEntity.ok("COMPLETE");

    public static final ResponseEntity<String> DUPLICATE_EMAIL = new ResponseEntity<>("이메일 중복", HttpStatus.CONFLICT);

    public static final ResponseEntity<String> DUPLICATE_NICKNAME = new ResponseEntity<>("닉네임 중복", HttpStatus.CONFLICT);

    public static final ResponseEntity<String> DUPLICATE_PHONE_NUMBER = new ResponseEntity<>("전화번호 중복", HttpStatus.CONFLICT);

    public static final ResponseEntity<String> USER_NOT_FOUND =
            new ResponseEntity<>(
                    "ID 또는 PW를 확인하세요.", HttpStatus.NOT_FOUND
            );

    public static final ResponseEntity<String> UNAUTHENTICATED = new ResponseEntity<>("로그인 후에 사용하세요.", HttpStatus.UNAUTHORIZED);
}
