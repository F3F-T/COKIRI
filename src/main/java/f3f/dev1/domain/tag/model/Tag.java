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

    @OneToMany(mappedBy = "tag", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<PostTag> postTags = new ArrayList<>();

    // 연관관계 편의 메소드
    public void addToPostTags(PostTag postTag) {
        this.postTags.add(postTag);
    }

    @Builder
    public Tag(Long id, String name) {
        this.id = id;
        this.name = name;
    }
}
