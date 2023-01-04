package f3f.dev1.domain.token.exception;

public class ExpireRefreshTokenException extends IllegalArgumentException {
    public ExpireRefreshTokenException() {
        super("EXPIRED_REFRESH_TOKEN");
    }

    public ExpireRefreshTokenException(String s) {
        super(s);
    }
}
