package f3f.dev1.domain.address.exception;

public class WrongUserException extends IllegalArgumentException {
    public WrongUserException() {
        super("잘못된 유저와 주소입니다");
    }

    public WrongUserException(String s) {
        super(s);
    }
}
