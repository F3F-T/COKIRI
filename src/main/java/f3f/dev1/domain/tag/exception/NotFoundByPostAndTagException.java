package f3f.dev1.domain.tag.exception;

public class NotFoundByPostAndTagException extends IllegalArgumentException{
    public NotFoundByPostAndTagException() {
        super("해당 게시글과 태그로 존재하는 PostTag가 없습니다.");
    }

    public NotFoundByPostAndTagException(String s) {
        super(s);
    }
}
