package f3f.dev1.domain.user.model;

import f3f.dev1.domain.message.model.Message;
import f3f.dev1.domain.model.Address;
import f3f.dev1.domain.model.UserBase;
import f3f.dev1.domain.post.model.Post;
import f3f.dev1.domain.scrap.model.Scrap;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
public class User extends UserBase {

    @Embedded
    private Address address;

    private String birthDate;

    private String phoneNumber;

    private String nickname;

    @OneToMany(mappedBy = "author", fetch = FetchType.LAZY)
    private List<Post> posts = new ArrayList<>();

    @OneToOne(mappedBy = "user")
    private Scrap scrap;

    @OneToMany(mappedBy = "sender", fetch = FetchType.LAZY)
    private List<Message> sendMessages = new ArrayList<>();

    @Builder
    public User(Long id, String email, String password, Address address, String birthDate, String phoneNumber, String nickname) {
        super(id, email, password);
        this.address = address;
        this.birthDate = birthDate;
        this.phoneNumber = phoneNumber;
        this.nickname = nickname;
    }
}
