package f3f.dev1.domain.comment.model;

import f3f.dev1.domain.comment.dto.CommentDTO;
import f3f.dev1.domain.member.model.Member;
import f3f.dev1.domain.post.model.Post;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

import static f3f.dev1.domain.comment.dto.CommentDTO.*;

@Entity
@Getter
@NoArgsConstructor
public class Comment {
    @Id
    @GeneratedValue
    @Column(name = "comment_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post post;

    private String content;

    private Long depth;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member author;

    @OneToMany(mappedBy = "parent", cascade = CascadeType.REMOVE)
    private List<Comment> childs = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "parent_id")
    private Comment parent;

    public void updateContent(String content) {
        this.content = content;
    }

    @Builder
    public Comment(Long id, Post post, String content, Long depth, Member author, Comment parent) {
        this.id = id;
        this.post = post;
        this.content = content;
        this.depth = depth;
        this.author = author;
        this.parent = parent;
    }

    public CommentInfoDto toInfoDto() {
        return CommentInfoDto.builder()
                .id(this.id)
                .postId(this.post.getId())
                .memberId(this.author.getId())
                .content(this.content)
                .depth(this.depth)
                .parentCommentId(this.parent.id)
                .build();
    }
}
