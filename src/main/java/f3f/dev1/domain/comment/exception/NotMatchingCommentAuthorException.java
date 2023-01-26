package f3f.dev1.domain.comment.exception;

public class NotMatchingCommentAuthorException extends IllegalArgumentException{
    public NotMatchingCommentAuthorException() {
        super("댓글 작성자가 사용자와 요청자와 않습니다.");
    }

    public NotMatchingCommentAuthorException(String s) {
        super(s);
    }
}
