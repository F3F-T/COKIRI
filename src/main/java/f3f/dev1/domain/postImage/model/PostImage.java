package f3f.dev1.domain.postImage.model;

import f3f.dev1.domain.post.model.Post;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class PostImage {
    @Id
    @GeneratedValue
    @Column(name = "img_id")
    private Long id;

    // 이미지 S3 경로
    private String imgPath;

    // TODO 사진 이름이 우리도 필요한가..? 필요 없을 듯 한데 조금 더 생각해봐야 할 것 같다.

    // 썸네일인지 아닌지 여부
    private Boolean isThumbnail;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    @JoinColumn(name = "post_id")
    private Post post;
}
