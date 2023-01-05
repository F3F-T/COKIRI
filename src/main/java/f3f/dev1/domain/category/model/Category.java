package f3f.dev1.domain.category.model;

import f3f.dev1.domain.post.model.Post;
import lombok.Builder;
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
    @OneToMany(mappedBy = "parent", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Category> child = new ArrayList<>();
    @OneToMany(mappedBy = "productCategory", fetch = FetchType.LAZY)
    private List<Post> products = new ArrayList<>();

    @OneToMany(mappedBy = "wishCategory", fetch = FetchType.LAZY)
    private List<Post> wishProducts = new ArrayList<>();

    @Builder
    public Category(Long id, String name, Long depth, Category parent) {

        this.id = id;
        this.name = name;
        this.depth = depth;
        this.parent = parent;
    }

    public void setDepth(Long depth){
        this.depth = depth;
    }

    public void updateCategoryName(String name){
        this.name = name;
    }

    public void updateCategoryDepth(Long depth) { this.depth = depth; }
}
