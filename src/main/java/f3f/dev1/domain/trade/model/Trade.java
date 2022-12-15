package f3f.dev1.domain.trade.model;

import f3f.dev1.domain.member.model.Member;
import f3f.dev1.domain.model.BaseTimeEntity;
import f3f.dev1.domain.model.TradeStatus;
import f3f.dev1.domain.post.model.Post;
import f3f.dev1.domain.trade.dto.TradeDTO.TradeInfoDto;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@Entity
@NoArgsConstructor
public class Trade extends BaseTimeEntity {
    @Id
    @GeneratedValue
    @Column(name = "trade_id")
    private Long id;

    @OneToOne
    @JoinColumn(name = "post_id")
    private Post post;

    @ManyToOne
    @JoinColumn(name = "seller_id")
    private Member seller;

    @ManyToOne
    @JoinColumn(name = "buyer_id")
    private Member buyer;

    private TradeStatus tradeStatus;

    @Builder
    public Trade(Long id, Post post, Member seller, Member buyer, TradeStatus tradeStatus) {
        this.id = id;
        this.post = post;
        this.seller = seller;
        this.buyer = buyer;
        this.tradeStatus = tradeStatus;
    }

    public TradeStatus updateTradeStatus(TradeStatus tradeStatus) {
        this.tradeStatus = tradeStatus;
        return tradeStatus;
    }

    public TradeInfoDto tradeInfoDto(String sellerNickname, String buyerNickname) {
        return TradeInfoDto.builder()
                .sellerNickname(sellerNickname)
                .buyerNickname(buyerNickname)
                .tradeStatus(tradeStatus).build();
    }

}
