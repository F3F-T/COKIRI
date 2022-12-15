package f3f.dev1.domain.trade.exception;

public class InvalidSellerIdException extends IllegalArgumentException {
    public InvalidSellerIdException() {
        super("판매자아이디와 게시글 작성자 아이디가 동일하지 않습니다.");
    }

    public InvalidSellerIdException(String s) {
        super(s);
    }
}
