package f3f.dev1.domain.member.exception;

public class UnknownLoginException extends IllegalArgumentException {
    public UnknownLoginException() {
        super("알 수 없는 로그인 형식입니다");
    }

    public UnknownLoginException(String s) {
        super(s);
    }
}


