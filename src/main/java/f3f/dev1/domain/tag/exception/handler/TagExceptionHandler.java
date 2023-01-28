package f3f.dev1.domain.tag.exception.handler;

import f3f.dev1.domain.tag.exception.DuplicateTagException;
import f3f.dev1.domain.tag.exception.NotFoundByPostAndTagException;
import f3f.dev1.domain.tag.exception.NotFoundByTagNameException;
import f3f.dev1.global.error.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import static f3f.dev1.domain.tag.exception.response.TagErrorResponse.*;

@Slf4j
@RestControllerAdvice
public class TagExceptionHandler {

    @ExceptionHandler(DuplicateTagException.class)
    protected final ResponseEntity<ErrorResponse> duplicateTagExceptionHandler (
            DuplicateTagException ex, WebRequest webRequest) {
        log.debug("duplicate tag exception  :: {}, detection time = {}", webRequest.getDescription(false));
        return DUPLICATE_TAG;
    }

    @ExceptionHandler(NotFoundByPostAndTagException.class)
    protected final ResponseEntity<ErrorResponse> notFoundByPostAndTagExceptionHandler (
            NotFoundByPostAndTagException ex, WebRequest webRequest) {
        log.debug("not found post and tag exception  :: {}, detection time = {}", webRequest.getDescription(false));
        return NOT_FOUND_BY_POST_AND_TAG;
    }

    @ExceptionHandler(NotFoundByTagNameException.class)
    protected final ResponseEntity<ErrorResponse> notFoundByTagNameExceptionHandler (
            DuplicateTagException ex, WebRequest webRequest) {
        log.debug("not found tag name exception  :: {}, detection time = {}", webRequest.getDescription(false));
        return NOT_FOUND_BY_TAG_NAME;
    }
}
