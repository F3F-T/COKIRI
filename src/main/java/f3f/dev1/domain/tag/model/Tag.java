package f3f.dev1.domain.tag.model;
import lombok.Builder;
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

    @OneToMany(mappedBy = "tag", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private List<PostTag> postTags = new ArrayList<>();

    @Builder
    public Tag(Long id, String name) {
        this.id = id;
        this.name = name;
    }
}
