package f3f.dev1.domain.tag.exception;

public class DuplicateTagException extends IllegalArgumentException{
    public DuplicateTagException(String message) {
        super(message);
    }
}
