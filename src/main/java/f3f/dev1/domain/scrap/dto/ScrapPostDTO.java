package f3f.dev1.domain.scrap.dto;

import com.querydsl.core.annotations.QueryProjection;
import f3f.dev1.domain.model.TradeStatus;
import lombok.*;

public class ScrapPostDTO {


    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Data
    public static class GetUserScrapPost{
        private Long postId;
        private String thumbNail;
        private String title;
        private TradeStatus tradeStatus;
        private String author;
        private Long price;
        private String productCategory;
        private String wishCategory;
        @QueryProjection
        public GetUserScrapPost(Long postId, String title, TradeStatus tradeStatus, String author, Long price, String productCategory, String wishCategory) {
            this.postId = postId;
            this.title = title;
            this.tradeStatus = tradeStatus;
            this.author = author;
            this.price = price;
            this.productCategory = productCategory;
            this.wishCategory = wishCategory;
        }
    }
}
