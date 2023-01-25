package f3f.dev1.domain.post.exception;

public class NotContainAuthorInfoException extends IllegalArgumentException{
    public NotContainAuthorInfoException(String s) {
        super(s);
    }

    public NotContainAuthorInfoException() {
        super("게시글 수정 정보에는 작성자 정보가 반드시 존재해야 합니다.");
    }
}
