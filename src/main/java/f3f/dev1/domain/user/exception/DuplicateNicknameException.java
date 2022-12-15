package f3f.dev1.domain.user.exception;

public class DuplicateNicknameException extends IllegalArgumentException {
    public DuplicateNicknameException() {
        super("닉네임 중복입니다.");
    }

    public DuplicateNicknameException(String s) {
        super(s);
    }
}
