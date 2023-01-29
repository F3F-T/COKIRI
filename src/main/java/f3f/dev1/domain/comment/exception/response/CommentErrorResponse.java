package f3f.dev1.domain.comment.exception.response;

import com.amazonaws.services.ec2.model.ResponseError;
import f3f.dev1.domain.comment.exception.DeletedCommentException;
import f3f.dev1.global.error.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class CommentErrorResponse {
    public static final ResponseEntity<ErrorResponse> DELETED_COMMENT = new ResponseEntity<>(
            ErrorResponse.builder().status(HttpStatus.NOT_FOUND).message("이미 삭제된 댓글입니다.").build(),
            HttpStatus.NOT_FOUND);

    public static final ResponseEntity<ErrorResponse> NOT_MATCHING_COMMENT_AUTHOR = new ResponseEntity<>(
            ErrorResponse.builder().status(HttpStatus.NOT_FOUND).message("요청자가 댓글 작성자 혹은 게시글 작성자가 아닙니다.").build(),
            HttpStatus.NOT_FOUND);
}
