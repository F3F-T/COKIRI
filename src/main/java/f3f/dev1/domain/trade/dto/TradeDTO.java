package f3f.dev1.domain.trade.dto;

import f3f.dev1.domain.member.model.Member;
import f3f.dev1.domain.model.TradeStatus;
import f3f.dev1.domain.post.model.Post;
import f3f.dev1.domain.trade.model.Trade;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class TradeDTO {
    @Builder
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class CreateTradeDto {
        private Long sellerId;

        private Long postId;

        public Trade toEntity(Member seller, Post post) {
            return Trade.builder()
                    .seller(seller)
                    .post(post)
                    .tradeStatus(TradeStatus.TRADABLE).build();
        }

    }

    @Builder
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class UpdateTradeDto {
        private Long postId;

        private Long userId;

        private TradeStatus tradeStatus;
    }


    @Builder
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class TradeInfoDto{
        private String sellerNickname;


        private TradeStatus tradeStatus;
    }

    @Builder
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class DeleteTradeDto {
        private Long postId;

        private Long userId;
    }

}
