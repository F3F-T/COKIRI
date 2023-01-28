package f3f.dev1.domain.message.model;

import f3f.dev1.domain.member.dto.MemberDTO;
import f3f.dev1.domain.message.dto.MessageRoomDTO;
import f3f.dev1.domain.model.BaseTimeEntity;
import f3f.dev1.domain.post.model.Post;
import f3f.dev1.domain.member.model.Member;
import f3f.dev1.domain.trade.model.Trade;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@NoArgsConstructor
public class MessageRoom extends BaseTimeEntity {

    @Id
    @GeneratedValue
    @Column(name = "messageRoom_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post post;

    @ManyToOne
    @JoinColumn(name="seller_id")
    private Member seller;

    @ManyToOne
    @JoinColumn(name = "buyer_id")
    private Member buyer;

    @OneToMany(mappedBy = "messageRoom", fetch = FetchType.LAZY)
    private List<Message> messages = new ArrayList<>();

    private boolean senderDelStatus;
    private boolean receiverDelStatus;

    public void setSenderDelStatus(boolean senderDelStatus){ this.senderDelStatus = senderDelStatus; }
    public void setReceiverDelStatus(boolean receiverDelStatus){this.receiverDelStatus = receiverDelStatus;}
    //생성할때!
    @Builder
    public MessageRoom(Long id, Post post, Member seller, Member buyer) {
        this.id = id;
        this.post = post;
        this.seller = seller;
        this.buyer = buyer;
        this.senderDelStatus = false;
        this.receiverDelStatus = false;
    }
    public MessageRoomDTO.MessageRoomInfoDto toMessageRoomInfo(){
        return MessageRoomDTO.MessageRoomInfoDto.builder()
                .id(this.getId())
                .sellerNickName(this.seller.getNickname())
                .buyerNickName(this.buyer.getNickname())
                .postId(this.post.getId())
                .sellerId(this.seller.getId())
                .buyerId(this.buyer.getId())
                .senderDelStatus(this.senderDelStatus)
                .receiverDelStatus(this.receiverDelStatus)
                .createTime(super.getCreateDate())
                .build();
    }
    // 철웅 추가
    public MessageRoomDTO.MessageRoomInfoWithOneDelStatus toMessageRoomInfoWithOneDelStatus(Member member, boolean delStatus) {
        return MessageRoomDTO.MessageRoomInfoWithOneDelStatus.builder()
                .id(this.id)
                .postTitle(post.getTitle())
                .memberNickname(member.getNickname())
                .delStatus(delStatus)
                .build();
    }

//    public MessageRoomDTO.MessageRoomInfoWithOneDelStatus toMessageRoomInfoWithOneDelStatus(){
//        return MessageRoomDTO.MessageRoomInfoWithOneDelStatus.builder()
//                .id(this.getId())
//                .postTitle(this.post.getTitle())
//                .memberNickname(this.seller.getNickname()||this.buyer.getNickname())
//                .delStatus(this.isSenderDelStatus()||this.isReceiverDelStatus())
//                .createTime(super.getCreateDate())
//                .build();
//
//    }
    public MessageRoomDTO.BuyingRoomInfoDto toBuyingRoomInfo(){
        return MessageRoomDTO.BuyingRoomInfoDto.builder()
                .id(this.getId())
                .PostTitle(this.post.getTitle())
                .sellerNickname(this.seller.getNickname())
                .senderDelStatus(this.isSenderDelStatus())
                .createTime(super.getCreateDate())
                .build();
    }

    public MessageRoomDTO.SellingRoomInfoDto toSellingRoomInfo(){
        return MessageRoomDTO.SellingRoomInfoDto.builder()
                .id(this.getId())
                .PostTitle(this.post.getTitle())
                .buyerNickname(this.buyer.getNickname())
                .receiverDelStatus(this.isReceiverDelStatus())
                .createTime(super.getCreateDate())
                .build();
    }
}

