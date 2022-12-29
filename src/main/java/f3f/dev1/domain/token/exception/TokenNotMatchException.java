package f3f.dev1.domain.token.exception;

public class TokenNotMatchException extends IllegalArgumentException {
    public TokenNotMatchException() {
        super("토큰의 유저 정보가 일치하지 않는다.");
    }

    public TokenNotMatchException(String s) {
        super(s);
    }
}
