package f3f.dev1.domain.message.model;

import f3f.dev1.domain.model.BaseTimeEntity;
import f3f.dev1.domain.post.model.Post;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@NoArgsConstructor
public class MessageRoom extends BaseTimeEntity {

    @Id
    @GeneratedValue
    @Column(name = "messageRoom_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post post;

    @OneToMany(mappedBy = "messageRoom", fetch = FetchType.LAZY)
    private List<Message> messages = new ArrayList<>();
}

