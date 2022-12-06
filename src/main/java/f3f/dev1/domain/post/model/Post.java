package f3f.dev1.domain.post.model;
import f3f.dev1.domain.message.model.MessageRoom;
import f3f.dev1.domain.user.model.User;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
@Entity
public class Post {
    @Id
    @GeneratedValue
    @Column(name = "post_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User author;

    private String title;

    private String content;

    @OneToMany(mappedBy = "post")
    private List<MessageRoom> messageRooms = new ArrayList<>();
}
