package f3f.dev1.domain.category.model;

import f3f.dev1.domain.post.model.Post;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@NoArgsConstructor
public class Category {

    @Id
    @GeneratedValue
    @Column(name = "category_id")
    private Long id;

    private String name;

    private Long depth;
    @ManyToOne
    private Category parent;
    @OneToMany(mappedBy = "parent", fetch = FetchType.LAZY)
    private List<Category> child = new ArrayList<>();
    @OneToMany(mappedBy = "category", fetch = FetchType.LAZY)
    private List<Post> posts = new ArrayList<>();
}
