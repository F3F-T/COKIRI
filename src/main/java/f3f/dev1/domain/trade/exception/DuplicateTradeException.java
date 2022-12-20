package f3f.dev1.domain.trade.exception;

public class DuplicateTradeException extends IllegalArgumentException {
    public DuplicateTradeException() {
        super("이미 거래가 생성되어있습니다");
    }

    public DuplicateTradeException(String s) {
        super(s);
    }
}
