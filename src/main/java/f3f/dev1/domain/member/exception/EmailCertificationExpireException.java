package f3f.dev1.domain.member.exception;

public class EmailCertificationExpireException extends IllegalArgumentException {
    public EmailCertificationExpireException() {
        super("코드가 만료되었습니다");
    }

    public EmailCertificationExpireException(String s) {
        super(s);
    }
}
