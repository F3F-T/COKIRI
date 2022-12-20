package f3f.dev1.domain.message.application;

import f3f.dev1.domain.message.dao.MessageRepository;
import f3f.dev1.domain.message.dao.MessageRoomRepository;
import f3f.dev1.domain.message.dto.MessageDTO;
import f3f.dev1.domain.message.exception.CanNotSendMessageByTradeStatus;
import f3f.dev1.domain.message.model.Message;
import f3f.dev1.domain.message.model.MessageRoom;
import f3f.dev1.domain.model.TradeStatus;
import f3f.dev1.domain.post.dao.PostRepository;
import f3f.dev1.domain.post.model.Post;
import f3f.dev1.domain.trade.dao.TradeRepository;
import f3f.dev1.domain.trade.model.Trade;
import f3f.dev1.domain.member.dao.MemberRepository;
import f3f.dev1.domain.member.exception.UserNotFoundException;
import f3f.dev1.domain.member.model.Member;
import f3f.dev1.global.error.exception.NotFoundByIdException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static f3f.dev1.domain.message.dto.MessageDTO.*;

/*
Message CRUD
C:
    - 포스트, 센더, 리시버 확인(유효한지 존재하는지)
    - 포스트 상태(거래중인지) 확인(물물교환이니까)
    -

R:
    -
U:
    -
D:
`   -
 */
@Service
@RequiredArgsConstructor
public class MessageService {
    private final MemberRepository memberRepository;
    private final PostRepository postRepository;
    private final MessageRepository messageRepository;
    private final MessageRoomRepository messageRoomRepository;
    private final TradeRepository tradeRepository;

    @Transactional
    public Long createMessage(MessageSaveRequest saveRequest) {

        Member sender = memberRepository.findByEmail(saveRequest.getSender().getEmail()).orElseThrow(UserNotFoundException::new);
        Member receiver = memberRepository.findByEmail(saveRequest.getReceiver().getEmail()).orElseThrow(UserNotFoundException::new);
        //포스트가 있는지 확인 포스트 레포지토리에서 findByAuthor쓰고 싶었는데 유저에서 확인하기도 하고 리스트가 와서 존재하는지만 확인하면 receiver는 당연히 자동적으로 연결되니까 확인 안해도 될듯?
       // Post post = postRepository.findById(saveRequest.getPost().getId()).orElseThrow(NotFoundByIdException::new);
        Trade trade = tradeRepository.findById(saveRequest.getPost().getTrade().getId()).orElseThrow(NotFoundByIdException::new);
        MessageRoom messageRoom = messageRoomRepository.findById(saveRequest.getPost().getId()).orElseThrow(NotFoundByIdException::new);


        //포스트에 메시지를 보낼 수 있는 상태인지 확인. (거래중이거나 완료이면 메시지를 보내짐 못함.)
        //메시지 룸에서도 확인해주나, 방이 만들어 진 뒤에는 확인을 못하니까 얘도 학인해주는게 좋을 듯 ->
        if(trade.getTradeStatus() != TradeStatus.TRADABLE){
            throw new CanNotSendMessageByTradeStatus();
        }

        Message message = saveRequest.toEntity();
        messageRepository.save(message);
        messageRoom.getMessages().add(message);

        return message.getId();
    }

}
