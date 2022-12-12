package f3f.dev1.domain.post.dto;

import f3f.dev1.domain.category.model.Category;
import f3f.dev1.domain.model.TradeStatus;
import f3f.dev1.domain.post.model.Post;
import f3f.dev1.domain.tag.model.PostTag;
import f3f.dev1.domain.trade.model.Trade;
import f3f.dev1.domain.user.model.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;


public class PostDTO {

    @Builder
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


    // Id로 찾아진 객체를 감싸서 뱉어주는 DTO
    @NoArgsConstructor
    @AllArgsConstructor
    public static class FindByAuthorPostListDTO {
        private List<Post> postList;
    }
}
