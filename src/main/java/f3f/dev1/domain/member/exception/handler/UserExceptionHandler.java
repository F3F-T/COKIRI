package f3f.dev1.domain.member.exception.handler;

import f3f.dev1.domain.member.exception.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import static f3f.dev1.domain.member.exception.response.UserErrorResponse.*;
@Slf4j
@RestControllerAdvice
public class UserExceptionHandler {
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

    @ExceptionHandler(UserNotFoundByUsernameAndPhoneException.class)
    protected final ResponseEntity<String> handleUserNotFoundByNameAndPhoneExeption(
            UserNotFoundByUsernameAndPhoneException ex, WebRequest request
    ) {
        log.debug("해당 이름과 전화번호로 존재하지 않는 유저", request.getDescription(false));
        return WRONG_USERNAME_PHONENUMBER;
    }

    @ExceptionHandler(DuplicatePhoneNumberExepction.class)
    protected final ResponseEntity<String> handleDuplicatePhoneNumberException(
            DuplicatePhoneNumberExepction ex, WebRequest request) {
        log.debug("Duplicate phonNumber :: {}, detection time = {}", request.getDescription(false));
        return DUPLICATE_PHONE_NUMBER;

    }

    @ExceptionHandler(UnauthenticatedUserException.class)
    protected final ResponseEntity<String> handleUnauthenticatedUserExeption(
            UnauthenticatedUserException ex, WebRequest request) {
        log.debug("로그인 한 후에 이용할 수 있는 서비스 입니다.", request.getDescription(false));
        return UNAUTHENTICATED;
    }
}
