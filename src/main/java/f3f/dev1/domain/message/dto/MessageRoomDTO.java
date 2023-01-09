package f3f.dev1.domain.message.dto;

import f3f.dev1.domain.message.model.Message;
import f3f.dev1.domain.message.model.MessageRoom;
import f3f.dev1.domain.post.model.Post;
import f3f.dev1.domain.member.model.Member;
import lombok.*;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;
import java.beans.Visibility;
import java.util.List;

public class MessageRoomDTO {

    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    public static class MessageRoomSaveRequest{

        @NonNull
        private Long postId;

        @NotNull
        private Long sellerId;

        @NonNull
        private Long buyerId;

        public MessageRoom toEntity(Post post, Member seller, Member buyer){
            return MessageRoom.builder()
                    .post(post)
                    .seller(seller)
                    .buyer(buyer)
                    .build();

        }
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DeleteMessageRoomRequest{
        private Long Id;
        private Long memberId;
        private Long postId;

    }


}
