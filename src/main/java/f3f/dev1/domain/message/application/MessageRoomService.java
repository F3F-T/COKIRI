package f3f.dev1.domain.message.application;

import f3f.dev1.domain.member.dao.MemberRepository;
import f3f.dev1.domain.member.model.Member;
import f3f.dev1.domain.message.dao.MessageRoomRepository;
import f3f.dev1.domain.message.dto.MessageDTO;
import f3f.dev1.domain.message.dto.MessageRoomDTO;
import f3f.dev1.domain.message.exception.CanNotSendMessageByTradeStatus;
import f3f.dev1.domain.message.model.MessageRoom;
import f3f.dev1.domain.model.TradeStatus;
import f3f.dev1.domain.post.dao.PostRepository;
import f3f.dev1.domain.post.model.Post;
import f3f.dev1.domain.trade.dao.TradeRepository;
import f3f.dev1.domain.trade.model.Trade;
import f3f.dev1.global.error.exception.NotFoundByIdException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static f3f.dev1.domain.message.dto.MessageDTO.*;
import static f3f.dev1.domain.message.dto.MessageRoomDTO.*;

/*
메시지룸을 만들고 메시지 만들기

 */
@Service
@RequiredArgsConstructor
public class MessageRoomService {

    private final MemberRepository memberRepository;
    private final PostRepository postRepository;
    private final TradeRepository tradeRepository;
    private final MessageRoomRepository messageRoomRepository;

    public Long createMessageRoom(MessageRoomSaveRequest saveRequest){
        Post post = postRepository.findById(saveRequest.getPost().getId()).orElseThrow(NotFoundByIdException::new);
        //파는 사람이 유효한지 확인(메시지를 받는 입장임)
        Member seller = memberRepository.findById(saveRequest.getSeller().getId()).orElseThrow(NotFoundByIdException::new);
        //메시지를 보내는 사람은 물건을 사고자하는 사람.
        Member buyer = memberRepository.findById(saveRequest.getBuyer().getId()).orElseThrow(NotFoundByIdException::new);
        //거래 상태 확인을 위해 포스트에 있는 트레이드 아이디로 가져옴.
        Trade trade = tradeRepository.findById(saveRequest.getPost().getTrade().getId()).orElseThrow(NotFoundByIdException::new);

        //거래 상태 확인하고 메시지 룸 만들기
        //바로 위에서 초기화하고 트레이드를 쓰는 게 나은지 아니면 그냥 포스트에서 타고 가는게 나은지지(post.getTrade().getTradeStatus()
        if(trade.getTradeStatus() != TradeStatus.TRADABLE){
            throw new CanNotSendMessageByTradeStatus();
        }

        MessageRoom messageRoom = saveRequest.toEntity();
        messageRoomRepository.save(messageRoom);
        post.getMessageRooms().add(messageRoom);

        return messageRoom.getId();
    }




}
