package f3f.dev1.domain.trade.dao;

import f3f.dev1.domain.trade.model.Trade;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

public interface TradeRepository extends JpaRepository<Trade, Long> {

    Optional<Trade> findById(Long id);

    boolean existsBySellerId(Long sellerId);
    List<Trade> findTradesBySellerId(Long sellerId);

    boolean existsByBuyerId(Long buyerId);

    List<Trade> findTradesByBuyerId(Long buyerId);

    boolean existsByPostId(Long postId);

    Optional<Trade> findByPostId(Long postId);

    boolean existsById(Long id);
}
