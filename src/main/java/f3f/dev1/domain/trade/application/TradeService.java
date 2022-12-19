package f3f.dev1.domain.trade.application;

import f3f.dev1.domain.member.dao.MemberRepository;
import f3f.dev1.domain.member.exception.NotAuthorizedException;
import f3f.dev1.domain.member.exception.UserNotFoundException;
import f3f.dev1.domain.member.model.Member;
import f3f.dev1.domain.model.TradeStatus;
import f3f.dev1.domain.post.dao.PostRepository;
import f3f.dev1.domain.post.model.Post;
import f3f.dev1.domain.trade.dao.TradeRepository;
import f3f.dev1.domain.trade.dto.TradeDTO.TradeInfoDto;
import f3f.dev1.domain.trade.exception.InvalidSellerIdException;
import f3f.dev1.domain.trade.model.Trade;
import f3f.dev1.global.error.exception.NotFoundByIdException;
import f3f.dev1.global.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static f3f.dev1.domain.trade.dto.TradeDTO.CreateTradeDto;
import static f3f.dev1.domain.trade.dto.TradeDTO.UpdateTradeDto;
import static f3f.dev1.global.common.constants.ResponseConstants.DELETE;

@Service
@RequiredArgsConstructor
public class TradeService {

    private final TradeRepository tradeRepository;

    private final MemberRepository memberRepository;

    private final PostRepository postRepository;


    // 트레이드 생성 메소드
    @Transactional
    public Long createTrade(CreateTradeDto createTradeDto, Long memberId) {
        Long sellerId = createTradeDto.getSellerId();
        Member seller = memberRepository.findById(sellerId).orElseThrow(NotFoundByIdException::new);
        Member buyer = memberRepository.findById(createTradeDto.getBuyerId()).orElseThrow(NotFoundByIdException::new);
        Post post = postRepository.findById(createTradeDto.getPostId()).orElseThrow(NotFoundByIdException::new);



        if (!seller.getId().equals(memberId)) {
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
    @Transactional(readOnly = true)
    public TradeInfoDto getTradeInfo(Long postId, Long memberId) {
        Optional<Trade> byId = tradeRepository.findByPostId(postId);
        if (byId.isPresent()) {
            Trade trade = byId.get();
            String sellerNickname = trade.getSeller().getNickname();

            String buyerNickname = trade.getBuyer().getNickname();
            return trade.tradeInfoDto(sellerNickname, buyerNickname);
        } else {
            String userNickname = memberRepository.findById(memberId).orElseThrow(UserNotFoundException::new).getNickname();
            return TradeInfoDto.builder()
                    .sellerNickname(userNickname)
                    .buyerNickname("none")
                    .tradeStatus(TradeStatus.TRADABLE).build();
        }
    }


    // 거래상태 업데이트 메서드
    @Transactional
    public TradeInfoDto updateTradeStatus(UpdateTradeDto updateTradeDto, Long memberId) {
        Trade trade = tradeRepository.findById(updateTradeDto.getTradeId()).orElseThrow(NotFoundByIdException::new);
        Member member = memberRepository.findById(updateTradeDto.getUserId()).orElseThrow(NotFoundByIdException::new);
        if (!member.getId().equals(memberId)) {
            throw new NotAuthorizedException();
        }
        trade.updateTradeStatus(updateTradeDto.getTradeStatus());
        String sellerNickname = trade.getSeller().getNickname();
        String buyerNickname = trade.getBuyer().getNickname();

        return trade.tradeInfoDto(sellerNickname, buyerNickname);
    }

    // 거래 삭제 메서드
    @Transactional
    public ResponseEntity<String> deleteTradeById(Long tradeId) {
        Trade trade = tradeRepository.findById(tradeId).orElseThrow(NotFoundByIdException::new);
        tradeRepository.delete(trade);
        return DELETE;
    }
}
