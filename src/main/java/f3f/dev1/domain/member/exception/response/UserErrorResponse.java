package f3f.dev1.domain.member.exception.response;

import f3f.dev1.global.error.exception.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class UserErrorResponse {
    public static final ResponseEntity<ErrorResponse> DUPLICATE_EMAIL = new ResponseEntity<>(ErrorResponse.builder().status(HttpStatus.CONFLICT).message("중복된 이메일입니다").build(), HttpStatus.CONFLICT);

    public static final ResponseEntity<ErrorResponse> DUPLICATE_NICKNAME = new ResponseEntity<>(ErrorResponse.builder().status(HttpStatus.CONFLICT).message("닉네임 중복이 발생하였습니다").build(), HttpStatus.CONFLICT);

    public static final ResponseEntity<ErrorResponse> DUPLICATE_PHONE_NUMBER = new ResponseEntity<>(ErrorResponse.builder().status(HttpStatus.CONFLICT).message("전화번호 중복이 발생하였습니다").build(), HttpStatus.CONFLICT);

    public static final ResponseEntity<ErrorResponse> USER_NOT_FOUND = new ResponseEntity<>(ErrorResponse.builder().status(HttpStatus.NOT_FOUND).message("사용자를 찾을 수 없습니다").build(), HttpStatus.NOT_FOUND);

    public static final ResponseEntity<ErrorResponse> WRONG_USERNAME_PHONENUMBER = new ResponseEntity<>(ErrorResponse.builder().status(HttpStatus.NOT_FOUND).message("해당 이름과 전화번호로 존재하는 유저는 없습니다.").build(), HttpStatus.NOT_FOUND);


    public static final ResponseEntity<ErrorResponse> UNAUTHENTICATED = new ResponseEntity<>(ErrorResponse.builder().status(HttpStatus.UNAUTHORIZED).message("로그인 후에 사용하세요.").build(), HttpStatus.UNAUTHORIZED);


    public static final ResponseEntity<ErrorResponse> INVALID_PASSWORD = new ResponseEntity<>(ErrorResponse.builder().status(HttpStatus.CONFLICT).message("잘못된 비밀번호 입력입니다.").build(), HttpStatus.CONFLICT);
}
