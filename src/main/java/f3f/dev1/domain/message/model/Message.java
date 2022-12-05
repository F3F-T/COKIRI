package f3f.dev1.domain.message.model;

import f3f.dev1.domain.user.model.User;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor
@Entity
public class Message {

    @Id
    @GeneratedValue
    @Column(name = "message_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User sender;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User receiver;
}
