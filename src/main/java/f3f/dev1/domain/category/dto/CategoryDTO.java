package f3f.dev1.domain.category.dto;

import f3f.dev1.domain.category.model.Category;
import f3f.dev1.domain.member.model.Member;
import f3f.dev1.domain.post.model.Post;
import lombok.*;

import javax.validation.constraints.Size;

public class CategoryDTO {

    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    public static class CategorySaveRequest {
        @NonNull
        @Size(min = 1, message="카테고리 이름은 한 글자 이상이어야합니다.")
        private String name;
        @NonNull
        private Long depth;
        @NonNull
        private Category parent;

        //유저 확인해야함. -> 둘다 아이디로 바꿔도 될듯?
        @NonNull
        private Member member;

        @NonNull
        private Post post;
//        @NonNull
//        private Long memberId;
//
//        @NonNull
//        private Long postId;

        public Category toEntity() {
            return Category.builder()
                    .name(this.name)
                    .depth(this.depth)
                    .parent(this.parent)
                    .build();
        }

    }


}