package f3f.dev1.domain.trade.model;

import f3f.dev1.domain.model.BaseTimeEntity;
import f3f.dev1.domain.model.TradeStatus;
import f3f.dev1.domain.post.model.Post;
import f3f.dev1.domain.user.model.User;
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
    private User seller;

    @ManyToOne
    @JoinColumn(name = "buyer_id")
    private User buyer;

    private TradeStatus tradeStatus;

    @Builder
    public Trade(Long id, Post post, User seller, User buyer, TradeStatus tradeStatus) {
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
}
