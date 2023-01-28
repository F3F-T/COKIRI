package f3f.dev1.domain.message.dto;

import f3f.dev1.domain.message.model.Message;
import f3f.dev1.domain.message.model.MessageRoom;
import f3f.dev1.domain.post.model.Post;
import f3f.dev1.domain.member.model.Member;
import lombok.*;
import org.apache.tomcat.jni.Local;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;
import java.beans.Visibility;
import java.time.LocalDateTime;
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
    @AllArgsConstructor
    @NoArgsConstructor
    public static class MessageRoomIdDto{
        private Long id;
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class DeleteMessageRoomRequest{
        private Long id;
        private Long memberId;
        private Long postId;
        //boolean deleteStatus;

    }

//    @Getter
//    @NoArgsConstructor
//    @AllArgsConstructor
//    @Builder
//    public static class DeleteMessageRoomInfoDto{
//        private Long id;
//        private boolean delStatus;
//    }

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
        private boolean senderDelStatus;
        private boolean receiverDelStatus;
        private LocalDateTime createTime;
        //private List<Message> messages;

    }
    //삭제가 해당 멤버에 맞게 하나만 나옴 -> 프론트가 편할듯
    //멤버 아이디, 닉네임은 내가 조회하는거면, 상대방것만 뜨게 한것!
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class MessageRoomInfoWithOneDelStatus{
        private Long id;
        private String postTitle;
        private String memberNickname;
        private boolean delStatus;
        private LocalDateTime createTime;
    }

    //파는 방이니까 이 사람은 receiver -> receiverDelStatus만 넣을것
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class SellingRoomInfoDto{
        private Long id;
        private String PostTitle;
        private String buyerNickname;
        private boolean receiverDelStatus;
        private LocalDateTime createTime;

    }

    //사는 방이니까 이 사람은 sender -> senderDelStatus만 넣을것
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class BuyingRoomInfoDto{
        private Long id;
        private String PostTitle;
        private String sellerNickname;
        private boolean senderDelStatus;
        private LocalDateTime createTime;
    }


}
