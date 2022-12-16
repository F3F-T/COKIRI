package f3f.dev1.domain.comment.exception;

public class DuplicateCommentException extends IllegalArgumentException{
    public DuplicateCommentException(String msg) {
        super(msg);
    }
}
