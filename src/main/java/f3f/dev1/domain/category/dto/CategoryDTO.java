package f3f.dev1.domain.category.dto;

import f3f.dev1.domain.category.model.Category;
import f3f.dev1.domain.post.model.Post;
import lombok.*;

import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

public class CategoryDTO {
    @Builder
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class CategorySaveRequest{

        @NonNull
        private String name;

        private Long depth;

        private Category parent;


        public Category toEntity(){
            return Category.builder()

                    .name(this.name)
                    .depth(this.depth)
                    .parent(this.parent)
                    .build();
        }


    }
}
