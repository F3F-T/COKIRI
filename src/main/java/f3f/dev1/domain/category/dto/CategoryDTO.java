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
        //@Size(min = 1, message="카테고리 이름은 한 글자 이상이어야합니다.")
        private String name;

        @NonNull
        private Long memberId;
        @NonNull
        private Long depth;
       // @NonNull -- 루트 카테고리를 위해 삭제
        private Long parentId;

        //유저 확인해야함. -> 둘다 아이디로 바꿔도 될듯?
//        @NonNull
//        private Member member;

//        @NonNull
////      private Post post; 포스트는 넣으면 안됨.

        public Category toEntity(Category parentCategory) {
            return Category.builder()
                    .name(this.name)
                    .depth(this.depth)
                    .parent(parentCategory)
                    .build();
        }

    }

//    @Getter
//    @NoArgsConstructor
//    @AllArgsConstructor
//    @Builder
//    public static class UpdateCategoryNameRequest{
//        private String name;
//        private String newName;
//    }


}