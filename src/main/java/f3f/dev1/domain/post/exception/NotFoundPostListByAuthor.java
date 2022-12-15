package f3f.dev1.domain.post.exception;

public class NotFoundPostListByAuthor extends IllegalArgumentException{
    public NotFoundPostListByAuthor(String message) {
        super(message);
    }
}
