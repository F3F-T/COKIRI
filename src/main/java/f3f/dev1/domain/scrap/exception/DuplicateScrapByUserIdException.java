package f3f.dev1.domain.scrap.exception;

public class DuplicateScrapByUserIdException extends IllegalArgumentException {
    public DuplicateScrapByUserIdException() {
        super("해당 유저 아이디로 이미 스크랩이 존재합니다.");
    }

    public DuplicateScrapByUserIdException(String s) {
        super(s);
    }
}
