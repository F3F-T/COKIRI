package f3f.dev1.domain.comment.exception;

public class DeletedCommentException extends IllegalArgumentException{
    public DeletedCommentException() {
        super("상위댓글 삭제 등으로 인해 삭제된 댓글입니다.");
    }

    public DeletedCommentException(String s) {
        super(s);
    }
}
