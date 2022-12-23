package f3f.dev1.domain.scrap.exception;

public class NotFoundPostInScrapException extends IllegalArgumentException {
    public NotFoundPostInScrapException() {
        super("스크랩에 해당 포스트가 존재하지 않습니다");
    }

    public NotFoundPostInScrapException(String s) {
        super(s);
    }
}
