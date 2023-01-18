package f3f.dev1.domain.post.exception;

public class NullOrderValueException extends IllegalArgumentException{
    public NullOrderValueException() {
        super("정렬 기준은 null이 되어선 안됩니다. (default = \"\" )");
    }

    public NullOrderValueException(String s) {
        super(s);
    }
}
