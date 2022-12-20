package f3f.dev1.domain.trade.api;

import f3f.dev1.domain.trade.application.TradeService;
import f3f.dev1.global.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static f3f.dev1.domain.trade.dto.TradeDTO.*;

@Slf4j
@RestController
@RequiredArgsConstructor
public class TradeController {
    private final TradeService tradeService;

    // TODO: 내뱉는 값 프론트랑 상의후 수정 예정
    // 트레이드 생성
    @PostMapping(value = "/trade")
    public ResponseEntity<Long> createTrade(@RequestBody CreateTradeDto createTradeDto) {
        Long trade = tradeService.createTrade(createTradeDto, SecurityUtil.getCurrentMemberId());
        return new ResponseEntity<>(trade, HttpStatus.CREATED);
    }
    // 트레이드 정보 조회
    @GetMapping(value = "/trade/{postId}")
    public ResponseEntity<TradeInfoDto> getTradeStatus(@PathVariable Long postId) {
        TradeInfoDto tradeInfo = tradeService.getTradeInfo(postId, SecurityUtil.getCurrentMemberId());

        return new ResponseEntity<>(tradeInfo, HttpStatus.OK);
    }
    // 거래 상태 변경
    // TODO: 프론트와 경로 상의후 수정 예정
    @PatchMapping(value = "/trade")
    public ResponseEntity<TradeInfoDto> updateTradeStatus(@RequestBody UpdateTradeDto updateTradeDto) {
        return new ResponseEntity<>(tradeService.updateTradeStatus(updateTradeDto, SecurityUtil.getCurrentMemberId()), HttpStatus.OK);

    }
    // TODO: 거래는 내부적으로 포스트 사라지면서 거래도 사라질거라고 생각되어서 따로 거래 삭제 API는 구현하지 않음

}
