package f3f.dev1.domain.message.model;

import f3f.dev1.domain.member.model.Member;
import f3f.dev1.domain.message.dto.MessageDTO;
import f3f.dev1.domain.message.dto.MessageRoomDTO;
import f3f.dev1.domain.model.BaseTimeEntity;
import f3f.dev1.domain.post.model.Post;
import f3f.dev1.domain.trade.model.Trade;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import javax.persistence.*;

import java.time.LocalDateTime;

import static f3f.dev1.domain.message.dto.MessageRoomDTO.*;

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

    private LocalDateTime createTime;


    @Builder
    public Message(Long id, String content, Member sender, Member receiver, MessageRoom messageRoom) {
        this.id = id;
        this.content = content;
        this.sender = sender;
        this.receiver = receiver;
        this.messageRoom = messageRoom;

    }

    public MessageDTO.MessageInfoDto toMessageInfo(){
        return MessageDTO.MessageInfoDto.builder()
                .id(this.getId())
                .senderNickname(this.sender.getNickname())
                .receiverNickname(this.receiver.getNickname())
                .content(this.content)
                .senderId(this.sender.getId())
                .receiverId(this.receiver.getId())
                .messageRoomId(this.messageRoom.getId())
                .createTime(super.getCreateDate())
                .build();


    }

}
