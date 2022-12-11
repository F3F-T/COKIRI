package f3f.dev1.domain.trade.dao;

import f3f.dev1.domain.trade.model.Trade;
import org.springframework.data.jpa.repository.JpaRepository;
public interface TradeRepository extends JpaRepository<Trade, Long> {
}
