package f3f.dev1.domain.user.exception;

public class UserNotFoundByUsernameAndPhoneException extends IllegalArgumentException {
    public UserNotFoundByUsernameAndPhoneException() {
        super("해당 이름과 전화번호로 존재하는 유저는 없습니다.");

    }

    public UserNotFoundByUsernameAndPhoneException(String s) {
        super(s);
    }
}
