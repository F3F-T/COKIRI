package f3f.dev1.domain.trade.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class TradeErrorResponse {
    public static final ResponseEntity<String> INVALID_SELLER = new ResponseEntity<>("판매자와 게시글 작성자가 일치하지 않습니다.", HttpStatus.CONFLICT);
}
