package f3f.dev1.domain.user.model;

import f3f.dev1.domain.message.model.Message;
import f3f.dev1.domain.model.UserBase;
import f3f.dev1.domain.post.model.Post;
import f3f.dev1.domain.scrap.model.Scrap;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
public class User extends UserBase {
    private String nickname;

    @OneToMany(mappedBy = "author", fetch = FetchType.LAZY)
    private List<Post> posts = new ArrayList<>();

    @OneToOne(mappedBy = "user")
    private Scrap scrap;

    @OneToMany(mappedBy = "sender", fetch = FetchType.LAZY)
    private List<Message> sendMessages = new ArrayList<>();

    @OneToMany(mappedBy = "receiver", fetch = FetchType.LAZY)
    private List<Message> receiveMessages = new ArrayList<>();
}
