package f3f.dev1.domain.comment.exception;

public class NotMatchingCommentAuthorException extends IllegalArgumentException{
    public NotMatchingCommentAuthorException() {
        super("요청자가 댓글 작성자 혹은 게시글 작성자가 아닙니다.");
    }

    public NotMatchingCommentAuthorException(String s) {
        super(s);
    }
}
