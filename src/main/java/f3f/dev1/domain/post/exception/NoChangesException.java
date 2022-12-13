package f3f.dev1.domain.post.exception;

public class NoChangesException extends IllegalArgumentException {
    public NoChangesException(String message) {
        super(message);
    }
}
