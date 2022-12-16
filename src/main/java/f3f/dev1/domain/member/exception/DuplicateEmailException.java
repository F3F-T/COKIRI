package f3f.dev1.domain.member.exception;

public class DuplicateEmailException extends IllegalArgumentException{
    public DuplicateEmailException() {super("이미 존재하는 이메일입니다.");}

    public DuplicateEmailException(String message) {
        super(message);
    }
}