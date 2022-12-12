package f3f.dev1.domain.trade.application;

import f3f.dev1.domain.post.dao.PostRepository;
import f3f.dev1.domain.post.model.Post;
import f3f.dev1.domain.trade.dao.TradeRepository;
import f3f.dev1.domain.trade.dto.TradeDTO;
import f3f.dev1.domain.trade.exception.InvalidSellerIdException;
import f3f.dev1.domain.trade.model.Trade;
import f3f.dev1.domain.user.dao.UserRepository;
import f3f.dev1.domain.user.model.User;
import f3f.dev1.global.common.constants.ResponseConstants;
import f3f.dev1.global.error.exception.NotFoundByIdException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static f3f.dev1.domain.trade.dto.TradeDTO.*;
import static f3f.dev1.domain.trade.dto.TradeDTO.CreateTradeDto;
import static f3f.dev1.global.common.constants.ResponseConstants.*;

@Service
@RequiredArgsConstructor
public class TradeService {

    private final TradeRepository tradeRepository;

    private final UserRepository userRepository;

    private final PostRepository postRepository;

    // 트레이드 생성 메소드
    @Transactional
    public Long createTrade(CreateTradeDto createTradeDto) {
        Long sellerId = createTradeDto.getSellerId();
        User seller = userRepository.findById(createTradeDto.getSellerId()).orElseThrow(NotFoundByIdException::new);
        User buyer = userRepository.findById(createTradeDto.getBuyerId()).orElseThrow(NotFoundByIdException::new);
        Post post = postRepository.findById(createTradeDto.getPostId()).orElseThrow(NotFoundByIdException::new);

        if (!sellerId.equals(post.getAuthor().getId())) {
            throw new InvalidSellerIdException();
        }
        Trade trade = createTradeDto.toEntity(seller, buyer, post);
        tradeRepository.save(trade);

        return trade.getId();
    }

    // 트레이드 조회 메소드
    // sellerId로 트레이드 조회
    @Transactional
    public List<Trade> findTradesBySellerId(Long sellerId) {
        if (tradeRepository.existsBySellerId(sellerId)) {
            return tradeRepository.findTradesBySellerId(sellerId);
        } else {
            throw new NotFoundByIdException();
        }
    }

    // buyerId로 트레이드 조회
    @Transactional
    public List<Trade> findTradesByBuyerId(Long buyerID) {
        if (tradeRepository.existsByBuyerId(buyerID)) {
            return tradeRepository.findTradesByBuyerId(buyerID);
        } else {
            throw new NotFoundByIdException();
        }
    }

    // postId로 트레이드 조회
    @Transactional
    public List<Trade> findTradesByPostId(Long postId) {
        if (tradeRepository.existsByPostId(postId)) {
            return tradeRepository.findTradesByPostId(postId);
        } else {
            throw new NotFoundByIdException();
        }
    }

    // 거래상태 업데이트 메서드
    @Transactional
    public String updateTradeStatus(UpdateTradeDto updateTradeDto) {
        Trade trade = tradeRepository.findById(updateTradeDto.getTradeId()).orElseThrow(NotFoundByIdException::new);

        trade.updateTradeStatus(updateTradeDto.getTradeStatus());
        return UPDATE;
    }

    // 거래 삭제 메서드
    @Transactional
    public String deleteTradeById(Long tradeId) {
        Trade trade = tradeRepository.findById(tradeId).orElseThrow(NotFoundByIdException::new);
        tradeRepository.delete(trade);
        return DELETE;
    }
}
