package f3f.dev1.domain.member.exception;

public class UnauthenticatedUserException extends IllegalArgumentException {
    public UnauthenticatedUserException() {
        super("등록되지 않은 유저입니다.");
    }

    public UnauthenticatedUserException(String s) {
        super(s);
    }
}
