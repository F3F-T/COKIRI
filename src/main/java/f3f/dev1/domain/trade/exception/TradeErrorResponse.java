package f3f.dev1.domain.trade.exception;

import f3f.dev1.global.error.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class TradeErrorResponse {
    public static final ResponseEntity<ErrorResponse> INVALID_SELLER = new ResponseEntity<>(ErrorResponse.builder().message("판매자와 게시글 작성자가 일치하지 않습니다.").status(HttpStatus.CONFLICT).build(), HttpStatus.CONFLICT);

    public static final ResponseEntity<ErrorResponse> DUPLICATE_TRADE = new ResponseEntity<>(ErrorResponse.builder().message("이미 존재하는 거래 입니다").status(HttpStatus.CONFLICT).build(), HttpStatus.CONFLICT);
}
