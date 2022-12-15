package f3f.dev1.domain.scrap.model;
import f3f.dev1.domain.post.model.ScrapPost;
import f3f.dev1.domain.member.model.Member;
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
    @JoinColumn(name = "member_id")
    private Member user;

    @OneToMany(mappedBy = "scrap", fetch = FetchType.LAZY)
    private List<ScrapPost> scrapPosts = new ArrayList<>();

    @Builder
    public Scrap(Long id, Member user) {
        this.id = id;
        this.user = user;
    }
}
