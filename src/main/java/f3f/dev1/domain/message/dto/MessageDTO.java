package f3f.dev1.domain.message.dto;

import f3f.dev1.domain.message.model.Message;
import f3f.dev1.domain.message.model.MessageRoom;
import f3f.dev1.domain.post.model.Post;
import f3f.dev1.domain.trade.model.Trade;
import f3f.dev1.domain.member.model.Member;
import lombok.*;

import java.time.LocalDateTime;

public class MessageDTO {
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    public static class MessageSaveRequest{


        private String content;

        @NonNull
        private Long senderId;

        private Long receiverId;

        private Long postId;

        @NonNull
        private Long messageRoomId;

        public Message toEntity(Member sender, Member receiver, Post post, MessageRoom messageRoom){
            return Message.builder()
                    .content(this.content)
                    .sender(sender)
                    .receiver(receiver)
                    .messageRoom(messageRoom)
                    .build();

        }
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DeleteMessage{
        private Long Id;
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DeleteMessageRequest{
        private Long id;
        private Long senderId;
        private Long messageRoomId;

    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class MessageInfoDto{
        private Long id;
        private String senderNickname;
        private String receiverNickname;
        private String content;
        private Long senderId;
        private Long receiverId;
        private Long messageRoomId;
        private LocalDateTime createTime;

    }
}
