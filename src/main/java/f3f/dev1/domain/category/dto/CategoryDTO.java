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
        private String name;

        @NonNull
        private Long memberId;

        private Long depth;

        private Long parentId;


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