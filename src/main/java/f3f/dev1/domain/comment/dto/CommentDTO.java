package f3f.dev1.domain.comment.dto;

import f3f.dev1.domain.comment.model.Comment;
import f3f.dev1.domain.post.model.Post;
import f3f.dev1.domain.user.model.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class CommentDTO {

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CreateParentCommentRequest {
        @NotNull
        private Long id;
        @NotNull
        private User author;
        @NotNull
        private Post post;
        @Size(min = 1, message = "댓글은 한글자 이상 작성해주세요")
        private String content;

        // child는 생성 시점에는 반드시 비어있으므로 따로 처리해주지 않음
        // TODO 피드백 : 이전에 피드백 받았을 때는 null을 할당하는걸 지양하라고 했는데, parent에 다이렉트로 null을 넣어도 문제가 없는지
        public Comment ToParentEntity() {
            return Comment.builder()
                    .post(this.post)
                    .author(this.author)
                    .content(this.content)
                    .depth(0L)
                    .parent(null)
                    .build();
        }
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CreateChildCommentRequest {
        @NotNull
        private Long id;
        @NotNull
        private User author;
        @NotNull
        private Post post;
        @Size(min = 1, message = "댓글은 한 글자 이상 작성해주세요")
        private String content;
        @NotNull
        private Comment parent;
        private Long depth;


        public Comment ToChildEntity() {
            return Comment.builder()
                    .post(this.post)
                    .author(this.author)
                    .content(this.content)
                    .depth(this.depth)
                    .parent(this.parent)
                    .build();
        }
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UpdateCommentRequest {
        @NotNull
        private Long id;
        @NotNull
        private User author;
        @NotNull
        private Post post;
        @Size(min = 1, message = "수정할 댓글을 한 글자 이상 적어주세요")
        private String content;
    }
}
