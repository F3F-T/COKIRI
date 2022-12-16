package f3f.dev1.domain.trade.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import static f3f.dev1.domain.trade.exception.TradeErrorResponse.*;


@Slf4j
@RestControllerAdvice
public class TradeExceptionHandler {
    @ExceptionHandler(InvalidSellerIdException.class)
    protected final ResponseEntity<String> handleInvalidSellerIdException(
            InvalidSellerIdException ex, WebRequest request) {
        log.debug("판매자 아이디와 게시글 작성자 아이디가 일치하지 않습니다.", request.getDescription(false));
        return INVALID_SELLER;
    }
}
