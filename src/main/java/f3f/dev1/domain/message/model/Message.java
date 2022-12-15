package f3f.dev1.domain.message.model;

import f3f.dev1.domain.member.model.Member;
import f3f.dev1.domain.model.BaseTimeEntity;
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
    private Member sender;

    @ManyToOne
    @JoinColumn(name = "receiver_id")
    private Member receiver;

    @ManyToOne
    @JoinColumn(name = "messageRoom_id")
    private MessageRoom messageRoom;


    @Builder
    public Message(Long id, String content, Member sender, MessageRoom messageRoom) {
        this.id = id;
        this.content = content;
        this.sender = sender;
        this.messageRoom = messageRoom;
    }
}
