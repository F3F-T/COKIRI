package f3f.dev1.domain.member.exception;

public class InvalidCertificationCodeException extends IllegalArgumentException {
    public InvalidCertificationCodeException() {
        super("코드가 잘못되었습니다");
    }

    public InvalidCertificationCodeException(String s) {
        super(s);
    }
}
