package f3f.dev1.domain.scrap.exception;

public class UserScrapNotFoundException extends IllegalArgumentException {
    public UserScrapNotFoundException() {
        super("유저에 스크랩이 존재하지 않습니다");
    }

    public UserScrapNotFoundException(String s) {
        super(s);
    }
}
