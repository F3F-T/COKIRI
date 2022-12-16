package f3f.dev1.domain.member.exception;

public class UserNotFoundException extends IllegalArgumentException {
    public UserNotFoundException() {
        super("사용자가 존재하지 않습니다");
    }

    public UserNotFoundException(String s) {
        super(s);
    }
}
