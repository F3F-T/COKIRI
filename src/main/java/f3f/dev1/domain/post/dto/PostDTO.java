package f3f.dev1.domain.post.dto;

import f3f.dev1.domain.category.model.Category;

import f3f.dev1.domain.member.model.Member;
import f3f.dev1.domain.model.TradeStatus;
import f3f.dev1.domain.post.model.Post;
import f3f.dev1.domain.tag.model.PostTag;
import f3f.dev1.domain.trade.model.Trade;
import lombok.*;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

import static f3f.dev1.domain.comment.dto.CommentDTO.*;
import static f3f.dev1.domain.member.dto.MemberDTO.*;


public class PostDTO {
    // C : Create 담당 DTO

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PostSaveRequest {


        private String title;

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

    // 최적화 문제 및 페이징을 위해 태그는 별도의 검색 로직으로 빼두겠다.
    // 그에 따라 태그를 제외한 검색 요청 DTO를 새로 만들겠음.
    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SearchPostRequestExcludeTag {
        String productCategory;
        String wishCategory;
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

        private String title;

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

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SinglePostInfoDto{

        private Long id;
        private String title;

        private String content;

        private Boolean tradeEachOther;

        private UserInfo userInfo;

        private String wishCategory;

        private String productCategory;

        private Long price;

        private TradeStatus tradeStatus;

        private List<CommentInfoDto> commentInfoDtoList;

        private List<String> tagNames;

        private Long scrapCount;

        private Long messageRoomCount;

        private LocalDateTime createdTime;
    }
}
