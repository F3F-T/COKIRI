package f3f.dev1.domain.post.model;

import f3f.dev1.domain.category.model.Category;
import f3f.dev1.domain.comment.model.Comment;
import f3f.dev1.domain.message.model.MessageRoom;
import f3f.dev1.domain.model.BaseTimeEntity;
import f3f.dev1.domain.tag.model.PostTag;
import f3f.dev1.domain.trade.model.Trade;
import f3f.dev1.domain.user.model.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
@Entity
public class Post extends BaseTimeEntity {
    @Id
    @GeneratedValue
    @Column(name = "post_id")
    private Long id;

    private String title;

    private String content;

    private Boolean tradeEachOther;
    @OneToOne(mappedBy = "post")
    private Trade trade;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User author;

    @OneToMany(mappedBy = "post", fetch = FetchType.LAZY)
    private List<MessageRoom> messageRooms = new ArrayList<>();

    @OneToMany(mappedBy = "post", fetch = FetchType.LAZY)
    private List<PostTag> postTags = new ArrayList<>();

    @OneToMany(mappedBy = "post", fetch = FetchType.LAZY)
    private List<ScrapPost> scrapPosts = new ArrayList<>();

    @OneToMany(mappedBy = "post", fetch = FetchType.LAZY)
    private List<Comment> comments = new ArrayList<>();

    @Builder
    public Post(Long id, String title, String content, Trade trade, Boolean tradeEachOther, Category category, User author) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.trade = trade;
        this.tradeEachOther = tradeEachOther;
        this.category = category;
        this.author = author;
    }
}
