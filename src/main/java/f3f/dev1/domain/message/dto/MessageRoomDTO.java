package f3f.dev1.domain.message.dto;

import f3f.dev1.domain.message.model.MessageRoom;
import f3f.dev1.domain.post.model.Post;
import f3f.dev1.domain.user.model.User;
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
    public static class SaveRequest{

        @NonNull
        private Post post;

        @NotNull
        private User seller;

        @NonNull
        private User buyer;

        public MessageRoom toEntity(){
            return MessageRoom.builder()
                    .post(this.post)
                    .seller(this.seller)
                    .buyer(this.buyer)
                    .build();

        }



    }

}
