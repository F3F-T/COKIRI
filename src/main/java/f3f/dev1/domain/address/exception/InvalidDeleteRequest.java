package f3f.dev1.domain.address.exception;

public class InvalidDeleteRequest extends IllegalArgumentException {
    public InvalidDeleteRequest() {
        super("잘못된 요청임");
    }

    public InvalidDeleteRequest(String s) {
        super(s);
    }
}
