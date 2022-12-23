package f3f.dev1.domain.token.exception;

public class LogoutUserException extends IllegalArgumentException {
    public LogoutUserException() {
        super("로그아웃된 사용자 입니다");
    }

    public LogoutUserException(String s) {
        super(s);
    }
}
