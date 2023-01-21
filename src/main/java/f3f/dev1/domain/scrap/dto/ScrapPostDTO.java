package f3f.dev1.domain.scrap.dto;

import com.querydsl.core.annotations.QueryProjection;
import f3f.dev1.domain.model.TradeStatus;
import f3f.dev1.domain.scrap.dao.ScrapPostRepository;
import lombok.*;

public class ScrapPostDTO {


    @Builder
//    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    public static class GetUserScrapPost{
        private Long postId;
        private String thumbNail;
        private String title;

        private TradeStatus tradeStatus;
//        private String tradeStatus;
        private String wishCategory;
        private Long scrapCount;
        @QueryProjection
        public GetUserScrapPost(Long postId, String title, String thumbNail, TradeStatus tradeStatus, String wishCategory, Long likeCount) {
            this.postId = postId;
            this.title = title;
            this.thumbNail = thumbNail;
            this.tradeStatus = tradeStatus;
            this.wishCategory = wishCategory;
            this.scrapCount = likeCount;
        }

//        public GetUserScrapPost(ScrapPostRepository.GetUserScrapPostInterface getUserScrapPostInterface) {
//            this.postId = getUserScrapPostInterface.getPostId();
//            this.thumbNail = getUserScrapPostInterface.getThumbnail();
//            this.title = getUserScrapPostInterface.getTitle();
//            this.tradeStatus = getUserScrapPostInterface.getTradeStatus();
//            this.wishCategory = getUserScrapPostInterface.getName();
//            this.scrapCount = getUserScrapPostInterface.getLikes();
//        }
    }
}
