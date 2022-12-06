package f3f.dev1.domain.post.model;

import f3f.dev1.domain.category.model.Category;
import f3f.dev1.domain.message.model.MessageRoom;
import f3f.dev1.domain.model.BaseTimeEntity;
import f3f.dev1.domain.model.TradeStatus;
import f3f.dev1.domain.tag.model.PostTag;
import f3f.dev1.domain.user.model.User;
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

    private TradeStatus tradeStatus;

    private Boolean tradeEachOther;

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
}
