package f3f.dev1.domain.user.exception;

public class UserNotFoundByEmailException extends UserNotFoundException {
    public UserNotFoundByEmailException() {
        super("이메일로 존재하는 유저가 없습니다.");
    }

    public UserNotFoundByEmailException(String s) {
        super(s);
    }
}
