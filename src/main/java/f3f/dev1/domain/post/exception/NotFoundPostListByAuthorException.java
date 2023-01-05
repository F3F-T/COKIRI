package f3f.dev1.domain.post.exception;

public class NotFoundPostListByAuthorException extends IllegalArgumentException{
    public NotFoundPostListByAuthorException(String message) {
        super(message);
    }
}
