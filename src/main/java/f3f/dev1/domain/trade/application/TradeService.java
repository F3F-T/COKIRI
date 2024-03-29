package f3f.dev1.domain.trade.application;

import f3f.dev1.domain.member.dao.MemberRepository;
import f3f.dev1.domain.member.exception.NotAuthorizedException;
import f3f.dev1.domain.member.exception.UserNotFoundException;
import f3f.dev1.domain.member.model.Member;
import f3f.dev1.domain.model.TradeStatus;
import f3f.dev1.domain.post.dao.PostRepository;
import f3f.dev1.domain.post.model.Post;
import f3f.dev1.domain.trade.dao.TradeRepository;
import f3f.dev1.domain.trade.dto.TradeDTO.DeleteTradeDto;
import f3f.dev1.domain.trade.dto.TradeDTO.TradeInfoDto;
import f3f.dev1.domain.trade.exception.DuplicateTradeException;
import f3f.dev1.domain.trade.exception.InvalidSellerIdException;
import f3f.dev1.domain.trade.model.Trade;
import f3f.dev1.global.error.exception.NotFoundByIdException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static f3f.dev1.domain.trade.dto.TradeDTO.CreateTradeDto;
import static f3f.dev1.domain.trade.dto.TradeDTO.UpdateTradeDto;

@Slf4j
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
        Post post = postRepository.findById(createTradeDto.getPostId()).orElseThrow(NotFoundByIdException::new);


        if (tradeRepository.existsByPostId(post.getId())) {
            throw new DuplicateTradeException();
        }

        if (!seller.getId().equals(memberId)) {
            throw new NotAuthorizedException();
        }

        if (!sellerId.equals(post.getAuthor().getId())) {
            throw new InvalidSellerIdException();
        }
        Trade trade = createTradeDto.toEntity(seller, post);
        tradeRepository.save(trade);

        return trade.getId();
    }

    // 트레이드 정보 조회 메서드
    @Transactional(readOnly = true)
    public TradeInfoDto getTradeInfo(Long postId) {
        Optional<Trade> byId = tradeRepository.findByPostId(postId);
        if (byId.isPresent()) {
            Trade trade = byId.get();
            String sellerNickname = trade.getSeller().getNickname();

            return trade.tradeInfoDto(sellerNickname);
        } else {
            Post post = postRepository.findById(postId).orElseThrow(NotFoundByIdException::new);
            String userNickname = memberRepository.findById(post.getAuthor().getId()).orElseThrow(UserNotFoundException::new).getNickname();
            return TradeInfoDto.builder()
                    .sellerNickname(userNickname)
                    .tradeStatus(TradeStatus.TRADABLE).build();
        }
    }


    // 거래상태 업데이트 메서드
    @Transactional
    public TradeInfoDto updateTradeStatus(UpdateTradeDto updateTradeDto, Long memberId) {
        Trade trade = tradeRepository.findByPostId(updateTradeDto.getPostId()).orElseThrow(NotFoundByIdException::new);
        if (!updateTradeDto.getUserId().equals(memberId)) {
            throw new NotAuthorizedException();
        }
        if (!updateTradeDto.getUserId().equals(trade.getSeller().getId())) {
            throw new InvalidSellerIdException();
        }
        trade.updateTradeStatus(updateTradeDto.getTradeStatus());
        String sellerNickname = trade.getSeller().getNickname();

        return trade.tradeInfoDto(sellerNickname);
    }

    // 거래 삭제 메서드
    @Transactional
    public TradeInfoDto deleteTrade(DeleteTradeDto deleteTradeDto, Long memberId) {
        Trade trade = tradeRepository.findByPostId(deleteTradeDto.getPostId()).orElseThrow(NotFoundByIdException::new);
        if (!deleteTradeDto.getUserId().equals(memberId)) {
            throw new NotAuthorizedException();
        }
        if (!deleteTradeDto.getUserId().equals(trade.getSeller().getId())) {
            throw new InvalidSellerIdException();
        }
        tradeRepository.delete(trade);
        Member member = memberRepository.findById(memberId).orElseThrow(NotFoundByIdException::new);

        return TradeInfoDto.builder()
                .sellerNickname(member.getNickname())
                .tradeStatus(TradeStatus.TRADABLE).build();
    }
}
