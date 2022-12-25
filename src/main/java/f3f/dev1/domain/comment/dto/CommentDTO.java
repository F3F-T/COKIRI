package f3f.dev1.domain.comment.dto;

import f3f.dev1.domain.comment.model.Comment;
import f3f.dev1.domain.post.model.Post;
import f3f.dev1.domain.member.model.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

public class CommentDTO {


    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CreateCommentRequest {
        @NotNull
        private Long authorId;
        @NotNull
        private Long postId;
        private Long depth;
        @Size(min = 1, message = "댓글은 한글자 이상 작성해주세요")
        private String content;
        private Long parentCommentId;

        public Comment toEntity(Post post, Member author, Comment parentComment) {
            return Comment.builder()
                    .post(post)
                    .author(author)
                    .content(this.content)
                    .depth(this.depth)
                    .parent(parentComment)
                    .build();
        }
    }


    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CreateParentCommentRequest {
        @NotNull
        private Long id;
        @NotNull
        private Long authorId;
        @NotNull
        private Long postId;
        @Size(min = 1, message = "댓글은 한글자 이상 작성해주세요")
        private String content;

        // child는 생성 시점에는 반드시 비어있으므로 따로 처리해주지 않음
        // TODO 피드백 : 이전에 피드백 받았을 때는 null을 할당하는걸 지양하라고 했는데, parent에 다이렉트로 null을 넣어도 문제가 없는지
        public Comment ToParentEntity(Post post, Member author) {
            return Comment.builder()
                    .post(post)
                    .author(author)
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
        private Long authorId;
        @NotNull
        private Long postId;
        @Size(min = 1, message = "댓글은 한 글자 이상 작성해주세요")
        private String content;
        @NotNull
        private Long parentCommentId;
        private Long depth;


        public Comment ToChildEntity(Post post, Member author, Comment parentComment) {
            return Comment.builder()
                    .post(post)
                    .author(author)
                    .content(this.content)
                    .depth(this.depth)
                    .parent(parentComment)
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
        private Long authorId;
        @NotNull
        private Long postId;
        @Size(min = 1, message = "수정할 댓글을 한 글자 이상 적어주세요")
        private String content;
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class FindByIdCommentResponse {
        @NotNull
        private Comment responseComment;
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DeleteCommentRequest {
        @NotNull
        private Long id;
        @NotNull
        private Long authorId;
        @NotNull
        private Long postId;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CommentInfoDto{
        private Long id;
        private Long postId;
        private Long memberId;
        private String content;
        private Long depth;
        private Long parentCommentId;
    }
}
