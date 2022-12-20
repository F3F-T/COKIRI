package f3f.dev1.domain.token.exception;

public class InvalidRefreshTokenException extends IllegalArgumentException {
    public InvalidRefreshTokenException() {
        super("Refresh Token이 유효하지 않습니다");
    }

    public InvalidRefreshTokenException(String s) {
        super(s);
    }
}
