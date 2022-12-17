package f3f.dev1.domain.message.dto;

import f3f.dev1.domain.message.model.Message;
import f3f.dev1.domain.message.model.MessageRoom;
import f3f.dev1.domain.post.model.Post;
import f3f.dev1.domain.trade.model.Trade;
import f3f.dev1.domain.member.model.Member;
import lombok.*;

public class MessageDTO {
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    public static class MessageSaveRequest{

        @NonNull
        private String content;

        @NonNull
        private Member sender;

        @NonNull
        private Member receiver;

        @NonNull
        private Post post;

        @NonNull
        private MessageRoom messageRoom;

        public Message toEntity(){
            return Message.builder()
                    .content(this.content)
                    .sender(this.sender)
                    .receiver(this.receiver)
                    .messageRoom(this.messageRoom)
                    .build();

        }
    }
}
