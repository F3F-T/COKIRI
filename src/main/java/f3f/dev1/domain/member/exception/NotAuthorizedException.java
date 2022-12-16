package f3f.dev1.domain.member.exception;

public class NotAuthorizedException extends IllegalArgumentException {
    public NotAuthorizedException() {
        super("접근 권한이 없습니다.");

    }

    public NotAuthorizedException(String s) {
        super(s);
    }
}
