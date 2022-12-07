package f3f.dev1.domain.user.model;

import f3f.dev1.domain.comment.model.Comment;
import f3f.dev1.domain.message.model.Message;
import f3f.dev1.domain.model.Address;
import f3f.dev1.domain.model.UserBase;
import f3f.dev1.domain.post.model.Post;
import f3f.dev1.domain.scrap.model.Scrap;
import f3f.dev1.domain.trade.model.Trade;

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

    @OneToMany(mappedBy = "author", fetch = FetchType.LAZY)
    private List<Comment> comments = new ArrayList<>();

    @OneToOne(mappedBy = "user")
    private Scrap scrap;

    @OneToMany(mappedBy = "sender", fetch = FetchType.LAZY)
    private List<Message> sendMessages = new ArrayList<>();

    @OneToMany(mappedBy = "receiver", fetch = FetchType.LAZY)
    private List<Message> receivedMessages = new ArrayList<>();

    @OneToMany(mappedBy = "buyer", fetch = FetchType.LAZY)
    private List<Trade> buyingTrades = new ArrayList<>();

    @OneToMany(mappedBy = "seller", fetch = FetchType.LAZY)
    private List<Trade> sellingTrades = new ArrayList<>();

    @Builder
    public User(Long id, String email, String password, Address address, String birthDate, String phoneNumber, String nickname) {
        super(id, email, password);
        this.address = address;
        this.birthDate = birthDate;
        this.phoneNumber = phoneNumber;
        this.nickname = nickname;
    }
}
