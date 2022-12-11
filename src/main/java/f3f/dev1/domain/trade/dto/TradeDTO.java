package f3f.dev1.domain.trade.dto;

import f3f.dev1.domain.model.TradeStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class TradeDTO {
    @Builder
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class CreateTradeDto{
        private Long sellerId;

        private Long buyerId;

        private Long postId;

    }

    @Builder
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class UpdateTradeDto {
        private Long tradeId;

        private TradeStatus tradeStatus;
    }



}
