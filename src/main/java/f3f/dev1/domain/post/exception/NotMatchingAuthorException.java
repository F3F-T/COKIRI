package f3f.dev1.domain.post.exception;

public class NotMatchingAuthorException extends IllegalArgumentException{
    public NotMatchingAuthorException(String message) {
        super(message);
    }
}
