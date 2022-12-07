package f3f.dev1.domain.message.model;

import f3f.dev1.domain.model.BaseTimeEntity;
import f3f.dev1.domain.user.model.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor
@Entity
public class Message extends BaseTimeEntity {

    @Id
    @GeneratedValue
    @Column(name = "message_id")
    private Long id;

    private String content;
    @ManyToOne
    @JoinColumn(name = "sender_id")
    private User sender;

    @ManyToOne
    @JoinColumn(name = "receiver_id")
    private User receiver;

    @ManyToOne
    @JoinColumn(name = "messageRoom_id")
    private MessageRoom messageRoom;


    @Builder
    public Message(Long id, String content, User sender, MessageRoom messageRoom) {
        this.id = id;
        this.content = content;
        this.sender = sender;
        this.messageRoom = messageRoom;
    }
}
