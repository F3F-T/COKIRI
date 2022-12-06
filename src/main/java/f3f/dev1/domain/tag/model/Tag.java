package f3f.dev1.domain.tag.model;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
@Entity
public class Tag {
    @Id
    @GeneratedValue
    @Column(name = "tag_id")
    private Long id;

    private String name;

    @OneToMany(mappedBy = "tag", fetch = FetchType.LAZY)
    private List<PostTag> postTags = new ArrayList<>();
}
