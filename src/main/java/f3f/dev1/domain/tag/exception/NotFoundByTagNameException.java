package f3f.dev1.domain.tag.exception;

public class NotFoundByTagNameException extends IllegalArgumentException{
    public NotFoundByTagNameException() {
        super("해당 게시글과 태그로 존재하는 PostTag가 없습니다.");
    }

    public NotFoundByTagNameException(String s) {
        super(s);
    }
}
