package f3f.dev1.domain.post.dto;

import f3f.dev1.domain.category.model.Category;
import f3f.dev1.domain.model.TradeStatus;
import f3f.dev1.domain.post.model.Post;
import f3f.dev1.domain.tag.model.PostTag;
import f3f.dev1.domain.trade.model.Trade;
import f3f.dev1.domain.user.model.User;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;


// TODO Builder가 필요없어보여서 포함하지 않았다
public class PostDTO {

    // C : Create 담당 DTO

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PostSaveRequest {

        @NotNull
        private Long id;
        @Size(min=2, max=20, message = "제목은 2글자 이상, 20자 이하로 설정해주세요")
        private String title;
        @NotBlank(message = "내용문을 작성해주세요")
        private String content;
        private Boolean tradeEachOther;
        @NotNull
        private User author;
        private Category productCategory;
        private Category wishCategory;

        @NotNull
        private Trade trade;

        public Post toEntity() {
            return Post.builder()
                    .title(this.title)
                    .content(this.content)
                    .tradeEachOther(tradeEachOther)
                    .author(this.author)
                    .productCategory(this.productCategory)
                    .wishCategory(this.wishCategory)
                    .trade(this.trade)
                    .build();
        }
    }


    // R : Read 담당 DTO들

    // Id로 찾아진 객체를 감싸서 뱉어주는 DTO
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class FindByAuthorPostListResponse {
        private List<Post> postList;
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class FindByIdPostResponse {
        private Post byIdPost;
    }

    // U : Update 담당 DTO들

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UpdatePostRequest {
        // 태그도 수정될 수 있으니 태그 리스트를 받은 뒤 Post의 UpdatePostTags에서 수정하도록 하겠다.
        // 카테고리도 같은 맥락
        @NotNull
        private Long id;
        @Size(min=2, max=20, message = "제목은 2글자 이상, 20자 이하로 설정해주세요")
        private String title;
        @NotBlank(message = "내용문을 작성해주세요")
        private String content;
        private Category productCategory;
        private Category wishCategory;
        private List<PostTag> postTags;
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DeletePostRequest {
        @NotNull
        private Long id;
        @NotNull
        private User requester;
    }
}
