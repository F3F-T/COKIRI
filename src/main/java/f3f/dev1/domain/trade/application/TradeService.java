package f3f.dev1.domain.trade.application;

import f3f.dev1.domain.model.TradeStatus;
import f3f.dev1.domain.post.dao.PostRepository;
import f3f.dev1.domain.post.model.Post;
import f3f.dev1.domain.trade.dao.TradeRepository;
import f3f.dev1.domain.trade.dto.TradeDTO;
import f3f.dev1.domain.trade.dto.TradeDTO.TradeInfoDto;
import f3f.dev1.domain.trade.exception.InvalidSellerIdException;
import f3f.dev1.domain.trade.model.Trade;
import f3f.dev1.domain.user.application.SessionLoginService;
import f3f.dev1.domain.user.dao.UserRepository;
import f3f.dev1.domain.user.exception.NotAuthorizedException;
import f3f.dev1.domain.user.model.User;
import f3f.dev1.global.error.exception.NotFoundByIdException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static f3f.dev1.domain.trade.dto.TradeDTO.CreateTradeDto;
import static f3f.dev1.domain.trade.dto.TradeDTO.UpdateTradeDto;
import static f3f.dev1.global.common.constants.ResponseConstants.DELETE;
import static f3f.dev1.global.common.constants.ResponseConstants.UPDATE;

@Service
@RequiredArgsConstructor
public class TradeService {

    private final TradeRepository tradeRepository;

    private final UserRepository userRepository;

    private final PostRepository postRepository;

    private final SessionLoginService sessionLoginService;

    // 트레이드 생성 메소드
    @Transactional
    public Long createTrade(CreateTradeDto createTradeDto) {
        Long sellerId = createTradeDto.getSellerId();
        User seller = userRepository.findById(createTradeDto.getSellerId()).orElseThrow(NotFoundByIdException::new);
        User buyer = userRepository.findById(createTradeDto.getBuyerId()).orElseThrow(NotFoundByIdException::new);
        Post post = postRepository.findById(createTradeDto.getPostId()).orElseThrow(NotFoundByIdException::new);


        // TODO: 세션에서 현재 로그인된 유저의 정보를 문제 없이 가져올 수 있으면 프론트에서도 seller 값을 안 넣어서 보내도 될 것 같음, 피드백 후 수정예정
        // TODO: 현재는 로그인할때 Response도 단순히 OK로 되어있음, 유저가 요청을 보내면 백엔드에서 세션에서 해당 유저 정보를 가져올 수 있다고 생각해서 프론트에게 현재 로그인한 유저에 대한 값을 안 넘김
        // TODO: 아래와 같은 검증이 필요하면 로그인 리턴 형식도 변경 예정
        if (!seller.getEmail().equals(sessionLoginService.getLoginUser())) {
            throw new NotAuthorizedException();
        }

        if (!sellerId.equals(post.getAuthor().getId())) {
            throw new InvalidSellerIdException();
        }
        Trade trade = createTradeDto.toEntity(seller, buyer, post);
        tradeRepository.save(trade);

        return trade.getId();
    }

    // 트레이드 정보 조회 메서드
    @Transactional
    public TradeInfoDto getTradeInfo(Long postId) {
        Optional<Trade> byId = tradeRepository.findByPostId(postId);
        if (byId.isPresent()) {
            Trade trade = byId.get();
            String sellerNickname = trade.getSeller().getNickname();

            String buyerNickname = trade.getBuyer().getNickname();
            return trade.tradeInfoDto(sellerNickname, buyerNickname);
        } else {
            return TradeInfoDto.builder()
                    .sellerNickname("none")
                    .buyerNickname("none")
                    .tradeStatus(TradeStatus.TRADABLE).build();
        }
    }
    // TODO: 필요 없는 메소드들은 피드백 후 삭제 혹은 추가 예정
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

    // 거래상태 업데이트 메서드
    @Transactional
    public ResponseEntity<String> updateTradeStatus(UpdateTradeDto updateTradeDto) {
        Trade trade = tradeRepository.findById(updateTradeDto.getTradeId()).orElseThrow(NotFoundByIdException::new);

        trade.updateTradeStatus(updateTradeDto.getTradeStatus());
        return UPDATE;
    }

    // 거래 삭제 메서드
    @Transactional
    public ResponseEntity<String> deleteTradeById(Long tradeId) {
        Trade trade = tradeRepository.findById(tradeId).orElseThrow(NotFoundByIdException::new);
        tradeRepository.delete(trade);
        return DELETE;
    }
}
