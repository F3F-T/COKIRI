package f3f.dev1.domain.post.model;

import f3f.dev1.domain.scrap.model.Scrap;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@Entity
@NoArgsConstructor
public class ScrapPost {
    @Id
    @GeneratedValue
    @Column(name = "scrapPost_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "scrap_id")
    private Scrap scrap;

    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post post;
}
