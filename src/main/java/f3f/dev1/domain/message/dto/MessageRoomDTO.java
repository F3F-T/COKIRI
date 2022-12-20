package f3f.dev1.domain.message.dto;

import f3f.dev1.domain.message.model.MessageRoom;
import f3f.dev1.domain.post.model.Post;
import f3f.dev1.domain.member.model.Member;
import lombok.*;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

public class MessageRoomDTO {

    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    public static class MessageRoomSaveRequest{

        @NonNull
        private Post post;

        @NotNull
        private Member seller;

        @NonNull
        private Member buyer;

        public MessageRoom toEntity(){
            return MessageRoom.builder()
                    .post(this.post)
                    .seller(this.seller)
                    .buyer(this.buyer)
                    .build();

        }
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DeleteMessageRoomRequest{
        private Long Id;
        private Long senderId;
        private Post post;
    }


}
