package f3f.dev1.domain.post.exception;

public class NotMatchingCommentException extends IllegalArgumentException{
    public NotMatchingCommentException(String message) {
        super(message);
    }
}
