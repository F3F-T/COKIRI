package f3f.dev1.domain.member.exception.handler;

import f3f.dev1.domain.member.exception.*;
import f3f.dev1.global.error.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import static f3f.dev1.domain.member.exception.response.UserErrorResponse.*;
@Slf4j
@RestControllerAdvice
public class UserExceptionHandler {
    @ExceptionHandler(DuplicateEmailException.class)
    protected final ResponseEntity<ErrorResponse> handleDuplicateEmailException(
            DuplicateEmailException ex, WebRequest request) {
        log.debug("Duplicate email :: {}, detection time = {}", request.getDescription(false));
        return DUPLICATE_EMAIL;
    }

    @ExceptionHandler(DuplicateNicknameException.class)
    protected final ResponseEntity<ErrorResponse> handleDuplicateNicknameException(
            DuplicateNicknameException ex, WebRequest request) {
        log.debug("Duplicate nickname :: {}, detection time = {}", request.getDescription(false));
        return DUPLICATE_NICKNAME;
    }

    @ExceptionHandler(UserNotFoundException.class)
    protected final ResponseEntity<ErrorResponse> handleUserNotFoundException(
            UserNotFoundException ex, WebRequest request) {
        log.debug("로그인 실패 : 존재하지 않는 이메일 혹은 비밀번호", request.getDescription(false));
        return USER_NOT_FOUND;
    }

    @ExceptionHandler(UserNotFoundByUsernameAndPhoneException.class)
    protected final ResponseEntity<ErrorResponse> handleUserNotFoundByNameAndPhoneExeption(
            UserNotFoundByUsernameAndPhoneException ex, WebRequest request
    ) {
        log.debug("해당 이름과 전화번호로 존재하지 않는 유저", request.getDescription(false));
        return WRONG_USERNAME_PHONENUMBER;
    }

    @ExceptionHandler(DuplicatePhoneNumberExepction.class)
    protected final ResponseEntity<ErrorResponse> handleDuplicatePhoneNumberException(
            DuplicatePhoneNumberExepction ex, WebRequest request) {
        log.debug("Duplicate phonNumber :: {}, detection time = {}", request.getDescription(false));
        return DUPLICATE_PHONE_NUMBER;

    }

    @ExceptionHandler(UnauthenticatedUserException.class)
    protected final ResponseEntity<ErrorResponse> handleUnauthenticatedUserException(
            UnauthenticatedUserException ex, WebRequest request) {
        log.debug("로그인 한 후에 이용할 수 있는 서비스 입니다.", request.getDescription(false));
        return UNAUTHENTICATED;
    }

    @ExceptionHandler(InvalidPasswordException.class)
    protected final ResponseEntity<ErrorResponse> handleInvalidPasswordException(
            InvalidPasswordException ex, WebRequest request
    ) {
        log.debug("잘못된 비밀번호 입력입니다.", request.getDescription(false));
        return INVALID_PASSWORD;
    }

    @ExceptionHandler(NotAuthorizedException.class)
    protected final ResponseEntity<ErrorResponse> handleNotAuthorizedException(
            NotAuthorizedException ex, WebRequest request
    ) {
        log.debug("권한이 없는 요청", request.getDescription(false));
        return new ResponseEntity<>(ErrorResponse.builder().status(HttpStatus.UNAUTHORIZED).message(ex.getMessage()).build(), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(EmailCertificationExpireException.class)
    protected final ResponseEntity<ErrorResponse> handleEmailCertificationExpireException(
            EmailCertificationExpireException ex, WebRequest request
    ){
        log.debug("만료된 이메일 코드", request.getDescription(false));
        return EMAIL_CERTIFICATION_EXPIRE;
    }

    @ExceptionHandler(InvalidCertificationCodeException.class)
    protected final ResponseEntity<ErrorResponse> handleInvalidCertificationCodeException(
            InvalidCertificationCodeException ex, WebRequest request
    ) {
        log.debug("잘못된 이메일 인증 코드", request.getDescription(false));
        return INVALID_CERTIFICATION;
    }
}
