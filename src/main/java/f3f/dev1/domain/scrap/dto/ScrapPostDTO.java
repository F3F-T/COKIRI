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
        private String wishCategory;
        private Long likeCount;
        @QueryProjection
        public GetUserScrapPost(Long postId, String title, TradeStatus tradeStatus, String wishCategory, Long likeCount) {
            this.postId = postId;
            this.title = title;
            this.tradeStatus = tradeStatus;
            this.wishCategory = wishCategory;
            this.likeCount = likeCount;
        }
    }
}
