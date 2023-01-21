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

//        @NotNull
//        private Long sellerId;

        @NonNull
        private Long buyerId;

        public MessageRoom toEntity(Post post, Member buyer){
            return MessageRoom.builder()
                    .post(post)
                    .seller(post.getAuthor())
                    .buyer(buyer)
                    .build();

        }
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DeleteMessageRoomRequest{
        private Long id;
        private Long memberId;
        private Long postId;
        //boolean deleteStatus;

    }

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class MessageRoomInfoDto{
        private Long id;
        private String sellerNickName;
        private String buyerNickName;
        private Long postId;
        private Long sellerId;
        private Long buyerId;
        //private List<Message> messages;

    }

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class SellingRoomInfoDto{
        private Long id;
        private String PostTitle;
        private String buyerNickname;
    }

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class BuyingRoomInfoDto{
        private Long id;
        private String PostTitle;
        private String sellerNickname;
    }


}
