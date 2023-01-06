package f3f.dev1.domain.post.dto;

import f3f.dev1.domain.category.model.Category;
import f3f.dev1.domain.member.model.Member;
import f3f.dev1.domain.model.TradeStatus;
import f3f.dev1.domain.post.model.Post;
import f3f.dev1.domain.tag.model.PostTag;
import f3f.dev1.domain.trade.model.Trade;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.List;


public class PostDTO {
    // C : Create 담당 DTO

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PostSaveRequest {

        @Size(min=2, max=20, message = "제목은 2글자 이상, 20자 이하로 설정해주세요")
        private String title;
        @NotBlank(message = "내용문을 작성해주세요")
        private String content;
        private Boolean tradeEachOther;
        @NotNull
        private Long authorId;
        private Long price;
        private String productCategory;
        private String wishCategory;
        @NotNull
        private List<String> tagNames;

        public Post toEntity(Member author, Category product, Category wish, List<PostTag> postTags) {
            return Post.builder()
                    .tradeEachOther(tradeEachOther)
                    .productCategory(product)
                    .content(this.content)
                    .wishCategory(wish)
                    .postTags(postTags)
                    .title(this.title)
                    .author(author)
                    .price(price)
                    .build();
        }
    }

    // R : read 담당 DTO들


    // 메인화면에서 조회에 사용될 DTO.
    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SearchPostRequest {
        String productCategory;
        String wishCategory;
        private List<String> tagNames;
        String minPrice;
        String maxPrice;
    }

    // U : Update 담당 DTO들

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UpdatePostRequest {
        // 태그도 수정될 수 있으니 태그 리스트를 받은 뒤 Post의 UpdatePostTags에서 수정하도록 하겠다.
        // 카테고리도 같은 맥락
        @NotNull
        private Long id;
        @NotNull
        private Long authorId;
        @Size(min=2, max=20, message = "제목은 2글자 이상, 20자 이하로 설정해주세요")
        private String title;
        @NotBlank(message = "내용문을 작성해주세요")
        private String content;
        private Long price;
        private String productCategory;
        private String wishCategory;
        private List<String> tagNames;
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DeletePostRequest {
        @NotNull
        private Long id;
        @NotNull
        private Long authorId;
    }
    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PostInfoDto{

        private Long id;
        private String title;

        private String content;

        private Boolean tradeEachOther;

        private String authorNickname;

        private String wishCategory;

        private Long price;

        private String productCategory;

        private TradeStatus tradeStatus;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PostInfoDtoWithTag{

        private Long id;
        private String title;

        private String content;

        private Boolean tradeEachOther;

        private String authorNickname;

        private String wishCategory;

        private String productCategory;

        private Long price;

        private TradeStatus tradeStatus;

        private List<String> tagNames;

        private Long scrapCount;

        private Long messageRoomCount;

        private LocalDateTime createdTime;
    }
}
