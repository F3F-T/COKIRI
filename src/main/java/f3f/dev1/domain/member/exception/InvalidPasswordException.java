package f3f.dev1.domain.member.exception;

public class InvalidPasswordException extends IllegalArgumentException {
    public InvalidPasswordException() {
        super("비밀번호가 잘못되었습니다.");
    }

    public InvalidPasswordException(String s) {
        super(s);
    }
}
