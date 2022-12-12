package f3f.dev1.domain.user.exception;

public class NotFoundByEmailException extends IllegalArgumentException {
    public NotFoundByEmailException() {
        super("이메일로 존재하는 유저가 없습니다.");
    }

    public NotFoundByEmailException(String s) {
        super(s);
    }
}
