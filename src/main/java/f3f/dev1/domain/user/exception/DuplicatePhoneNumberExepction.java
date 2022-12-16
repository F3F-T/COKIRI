package f3f.dev1.domain.user.exception;

public class DuplicatePhoneNumberExepction extends IllegalArgumentException {
    public DuplicatePhoneNumberExepction() {
        super("전화번호 중복입니다.");
    }

    public DuplicatePhoneNumberExepction(String s) {
        super(s);
    }
}
