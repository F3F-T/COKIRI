package f3f.dev1.domain.scrap.model;
import f3f.dev1.domain.post.model.ScrapPost;
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
public class Scrap {
    @Id
    @GeneratedValue
    @Column(name = "scrap_id")
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "scrap", fetch = FetchType.LAZY)
    private List<ScrapPost> scrapPosts = new ArrayList<>();

    @Builder
    public Scrap(Long id, User user) {
        this.id = id;
        this.user = user;
    }
}
