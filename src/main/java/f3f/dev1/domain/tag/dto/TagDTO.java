package f3f.dev1.domain.tag.dto;

import f3f.dev1.domain.post.model.Post;
import f3f.dev1.domain.tag.model.Tag;
import f3f.dev1.domain.member.model.Member;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

public class TagDTO {

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CreateTagRequest {

        @NotNull
        @Size(max = 10, message = "태그는 10글자 이하로 작성해주세요")
        private String name;

        private Long authorId;

        public Tag toEntity() {
            return Tag.builder()
                    .name(this.name)
                    .build();
        }
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AddTagToPostRequest {
        @NotNull
        private Long tagId;
        @NotNull
        private Long postId;
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class GetPostListByTagIdRequest {
        @NotNull
        private Long id;
    }

//    @Getter
//    @NoArgsConstructor
//    @AllArgsConstructor
//    public static class GetPostListByTagNameRequest {
//        @NotNull
//        private String name;
//    }

//    @Getter
//    @NoArgsConstructor
//    @AllArgsConstructor
//    public static class GetPostListByTagResponse {
//        @NotNull
//        private List<Post> postList;
//    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DeleteTagFromPostRequest {
        @NotNull
        private Long id;
        @NotNull
        private Post post;
        @NotNull
        private Member author;
    }
}
