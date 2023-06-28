package f3f.dev1.domain.token.exception.response;

public class ExpireAccessTokenException extends IllegalArgumentException {
    public ExpireAccessTokenException() {
        super("만료된 엑세스 토큰");
    }

    public ExpireAccessTokenException(String s) {
        super(s);
    }
}

