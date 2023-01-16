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
import java.time.LocalDateTime;
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
    public static class UpdateCommentRequest {
        @NotNull
        private Long id;
        @NotNull
        private Long parentId;
        @NotNull
        private Long authorId;
        @NotNull
        private Long postId;

        private String content;
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

        private String memberNickname;

        private String imageUrl;

        private String content;

        private Long depth;

        private Long parentCommentId;

        private LocalDateTime createdTime;
    }
}
