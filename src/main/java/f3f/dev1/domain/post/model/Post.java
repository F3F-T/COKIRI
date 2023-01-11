package f3f.dev1.domain.post.model;

import f3f.dev1.domain.category.model.Category;
import f3f.dev1.domain.comment.dto.CommentDTO;
import f3f.dev1.domain.comment.model.Comment;
import f3f.dev1.domain.member.dto.MemberDTO;
import f3f.dev1.domain.member.model.Member;
import f3f.dev1.domain.message.model.MessageRoom;
import f3f.dev1.domain.model.BaseTimeEntity;
import f3f.dev1.domain.model.TradeStatus;
import f3f.dev1.domain.post.dto.PostDTO;
import f3f.dev1.domain.tag.model.PostTag;
import f3f.dev1.domain.trade.model.Trade;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

import static f3f.dev1.domain.comment.dto.CommentDTO.*;
import static f3f.dev1.domain.member.dto.MemberDTO.*;
import static f3f.dev1.domain.post.dto.PostDTO.*;

@Getter
@NoArgsConstructor
@Entity
public class Post extends BaseTimeEntity {
    @Id
    @GeneratedValue
    @Column(name = "post_id")
    private Long id;

    private String title;

    private String content;

    // 끼리끼리 거래 여부
    private Boolean tradeEachOther;

    @OneToOne(mappedBy = "post", cascade = CascadeType.REMOVE)
    private Trade trade;

    private Long price;

    @ManyToOne
    @JoinColumn(name = "productCategory_id")
    private Category productCategory;

    @ManyToOne
    @JoinColumn(name = "wishCategory_id")
    private Category wishCategory;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member author;

    @OneToMany(mappedBy = "post", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private List<MessageRoom> messageRooms = new ArrayList<>();

    @OneToMany(mappedBy = "post", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private List<PostTag> postTags = new ArrayList<>();

    @OneToMany(mappedBy = "post", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private List<ScrapPost> scrapPosts = new ArrayList<>();

    @OneToMany(mappedBy = "post", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private List<Comment> comments = new ArrayList<>();

    public void updatePostInfos(UpdatePostRequest updatePostRequest, Category productCategory, Category wishCategory, List<PostTag> postTags) {
        this.title = updatePostRequest.getTitle();
        this.content = updatePostRequest.getContent();
        this.price = updatePostRequest.getPrice();
        this.postTags = postTags;
        this.productCategory = productCategory;
        this.wishCategory = wishCategory;
    }

    @Builder
    public Post(Long id, String title, String content, Boolean tradeEachOther, Category productCategory, Category wishCategory, Member author, List<PostTag> postTags, Long price) {
        this.productCategory = productCategory;
        this.tradeEachOther = tradeEachOther;
        this.wishCategory = wishCategory;
        this.postTags = postTags;
        this.content = content;
        this.author = author;
        this.price = price;
        this.title = title;
        this.id = id;
    }


    public PostInfoDto toInfoDto() {
        TradeStatus tradeStatus;
        if (this.trade == null) {
            tradeStatus = TradeStatus.TRADABLE;
        } else {
            tradeStatus = this.trade.getTradeStatus();
        }
        return PostInfoDto.builder()
                .id(this.id)
                .authorNickname(this.author.getNickname())
                .content(this.content)
                .title(this.title)
                .price(this.price)
                .productCategory(this.productCategory.getName())
                .wishCategory(this.wishCategory.getName())
                .tradeEachOther(this.tradeEachOther)
                .tradeStatus(tradeStatus)
                .build();
    }

    public PostInfoDtoWithTag toInfoDtoWithTag(List<String> tagNames, Long scrapCount, Long messageRoomCount) {
        TradeStatus tradeStatus;
        if (this.trade == null) {
            tradeStatus = TradeStatus.TRADABLE;
        } else {
            tradeStatus = this.trade.getTradeStatus();
        }
        return PostInfoDtoWithTag.builder()
                .productCategory(this.productCategory.getName())
                .authorNickname(this.author.getNickname())
                .wishCategory(this.wishCategory.getName())
                .tradeEachOther(this.tradeEachOther)
                .createdTime(super.getCreateDate())
                .messageRoomCount(messageRoomCount)
                .tradeStatus(tradeStatus)
                .scrapCount(scrapCount)
                .content(this.content)
                .tagNames(tagNames)
                .title(this.title)
                .price(this.price)
                .id(this.id)
                .build();
    }

    public SinglePostInfoDto toSinglePostInfoDto(List<String> tagNames, Long scrapCount, Long messageRoomCount, UserInfo userInfo, List<CommentInfoDto> commentInfoDtoList) {
//        TradeStatus tradeStatus;
//        if (this.trade == null) {
//            tradeStatus = TradeStatus.TRADABLE;
//        } else {
//            tradeStatus = this.trade.getTradeStatus();
//        }
        return SinglePostInfoDto.builder()
                .productCategory(this.productCategory.getName())
                .wishCategory(this.wishCategory.getName())
                .commentInfoDtoList(commentInfoDtoList)
                .tradeEachOther(this.tradeEachOther)
                .createdTime(super.getCreateDate())
                .messageRoomCount(messageRoomCount)
                .tradeStatus(this.trade.getTradeStatus())
                .scrapCount(scrapCount)
                .content(this.content)
                .userInfo(userInfo)
                .tagNames(tagNames)
                .title(this.title)
                .price(this.price)
                .id(this.id)
                .build();
    }
}
