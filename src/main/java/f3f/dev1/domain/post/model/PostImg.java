package f3f.dev1.domain.post.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class PostImg {
    @Id
    @GeneratedValue
    @Column(name = "img_id")
    private Long id;

    // 이미지 S3 경로
    private String imgPath;

    // 썸네일인지 아닌지 여부
    private Boolean isThumbnail;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    @JoinColumn(name = "post_id")
    private Post post;
}
