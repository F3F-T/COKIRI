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

    // 끼리끼리 거래 여부
    private Boolean tradeEachOther;
    @OneToOne(mappedBy = "post")
    private Trade trade;

    @ManyToOne
    @JoinColumn(name = "productCategory_id")
    private Category productCategory;

    @ManyToOne
    @JoinColumn(name = "wishCategory_id")
    private Category wishCategory;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User author;

    @OneToMany(mappedBy = "post", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private List<MessageRoom> messageRooms = new ArrayList<>();

    @OneToMany(mappedBy = "post", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private List<PostTag> postTags = new ArrayList<>();

    @OneToMany(mappedBy = "post", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private List<ScrapPost> scrapPosts = new ArrayList<>();

    @OneToMany(mappedBy = "post", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private List<Comment> comments = new ArrayList<>();

    public void updateTitle(String title) {
        this.title = title;
    }

    public void updateContent(String content) {
        this.content = content;
    }

    public void updatePostTags(List<PostTag> postTags) {
        this.postTags = postTags;
    }

    public void updateProductCategory(Category productCategory) {
        this.productCategory = productCategory;
    }

    public void updateWishCategory(Category wishCategory) {
        this.wishCategory = wishCategory;
    }

    @Builder
    public Post(Long id, String title, String content, Trade trade, Boolean tradeEachOther, Category productCategory, Category wishCategory, User author) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.trade = trade;
        this.tradeEachOther = tradeEachOther;
        this.productCategory = productCategory;
        this.wishCategory = wishCategory;
        this.author = author;
    }


}
