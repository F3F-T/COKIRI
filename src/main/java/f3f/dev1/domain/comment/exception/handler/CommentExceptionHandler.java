package f3f.dev1.domain.comment.exception.handler;

import f3f.dev1.domain.comment.exception.DeletedCommentException;
import f3f.dev1.domain.comment.exception.NotMatchingCommentAuthorException;
import f3f.dev1.global.error.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import static f3f.dev1.domain.comment.exception.response.CommentErrorResponse.*;

@Slf4j
@RestControllerAdvice
public class CommentExceptionHandler {

    @ExceptionHandler(DeletedCommentException.class)
    protected final ResponseEntity<ErrorResponse> handleDeletedCommentException(
            DeletedCommentException ex, WebRequest webRequest) {
        log.debug("deleted comment exception  :: {}, detection time = {}", webRequest.getDescription(false));
        return DELETED_COMMENT;
    }

    @ExceptionHandler(NotMatchingCommentAuthorException.class)
    protected final ResponseEntity<ErrorResponse> handleNotMatchingCommentAuthorException(
            NotMatchingCommentAuthorException ex, WebRequest webRequest) {
        log.debug("not matching comment author exception  :: {}, detection time = {}", webRequest.getDescription(false));
        return NOT_MATCHING_COMMENT_AUTHOR;
    }
}
