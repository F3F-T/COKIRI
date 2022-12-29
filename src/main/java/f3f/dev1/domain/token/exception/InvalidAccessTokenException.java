package f3f.dev1.domain.token.exception;

public class InvalidAccessTokenException extends IllegalArgumentException {
    public InvalidAccessTokenException() {
        super("잘못된 엑세스 토큰입니다");
    }

    public InvalidAccessTokenException(String s) {
        super(s);
    }
}
