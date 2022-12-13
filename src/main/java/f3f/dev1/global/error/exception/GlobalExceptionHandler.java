package f3f.dev1.global.error.exception;

import f3f.dev1.domain.user.exception.DuplicateEmailException;
import f3f.dev1.domain.user.exception.DuplicateNicknameException;
import f3f.dev1.domain.user.exception.DuplicatePhoneNumberExepction;
import f3f.dev1.domain.user.exception.UserNotFoundException;
import f3f.dev1.global.common.constants.ResponseConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import static f3f.dev1.global.common.constants.ResponseConstants.*;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(DuplicateEmailException.class)
    protected final ResponseEntity<String> handleDuplicateEmailException(
            DuplicateEmailException ex, WebRequest request) {
        log.debug("Duplicate email :: {}, detection time = {}", request.getDescription(false));
        return DUPLICATE_EMAIL;
    }

    @ExceptionHandler(DuplicateNicknameException.class)
    protected final ResponseEntity<String> handleDuplicateNicknameException(
            DuplicateNicknameException ex, WebRequest request) {
        log.debug("Duplicate nickname :: {}, detection time = {}", request.getDescription(false));
        return DUPLICATE_NICKNAME;
    }

    @ExceptionHandler(UserNotFoundException.class)
    protected final ResponseEntity<String> handleUserNotFoundException(
            UserNotFoundException ex, WebRequest request) {
        log.debug("로그인 실패 : 존재하지 않는 이메일 혹은 비밀번호", request.getDescription(false));
        return USER_NOT_FOUND;
    }

    @ExceptionHandler(DuplicatePhoneNumberExepction.class)
    protected final ResponseEntity<String> handleDuplicatePhoneNumberException(
            DuplicatePhoneNumberExepction ex, WebRequest request) {
        log.debug("Duplicate phonNumber :: {}, detection time = {}", request.getDescription(false));
        return DUPLICATE_PHONENUMBER;

    }
}
