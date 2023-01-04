package f3f.dev1.domain.message.application;

import f3f.dev1.domain.message.dao.MessageRepository;
import f3f.dev1.domain.message.dao.MessageRoomRepository;
import f3f.dev1.domain.message.dto.MessageDTO;
import f3f.dev1.domain.message.exception.CanNotDeleteMessage;
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
    public Long createMessage(MessageSaveRequest messageSaveRequest) {

        Member sender = memberRepository.findByEmail(messageSaveRequest.getSender().getEmail()).orElseThrow(UserNotFoundException::new);
        Member receiver = memberRepository.findByEmail(messageSaveRequest.getReceiver().getEmail()).orElseThrow(UserNotFoundException::new);
        //포스트가 있는지 확인 포스트 레포지토리에서 findByAuthor쓰고 싶었는데 유저에서 확인하기도 하고 리스트가 와서 존재하는지만 확인하면 receiver는 당연히 자동적으로 연결되니까 확인 안해도 될듯?
        Post post = postRepository.findById(messageSaveRequest.getPost().getId()).orElseThrow(NotFoundByIdException::new);
        Trade trade = tradeRepository.findByPostId(messageSaveRequest.getPost().getId()).orElseThrow(NotFoundByIdException::new);

        MessageRoom messageRoom = messageRoomRepository.findById(messageSaveRequest.getPost().getId()).orElseThrow(NotFoundByIdException::new);


        //포스트에 메시지를 보낼 수 있는 상태인지 확인. (거래중이거나 완료이면 메시지를 보내짐 못함.)
        if(trade.getTradeStatus() != TradeStatus.TRADABLE){
            throw new CanNotSendMessageByTradeStatus();
        }

        Message message = messageSaveRequest.toEntity();
        messageRepository.save(message);
        messageRoom.getMessages().add(message);
        sender.getSendMessages().add(message);
        receiver.getReceivedMessages().add(message);

        return message.getId();
    }

    //메시지는 나중에~ 고도화때 ?
    //TODO 메시지 삭제는 카톡과 같이 5분 내에 전송시 삭제해야되나?(양방향 삭제 논의)!!!!!
    //자기가 보낸것만 삭제할 수 있음.
    //TODO 채팅방 지우는 형식은 카톡과 동일하게 양쪽 따로 관리하도록 해야할듯.
    @Transactional
    public String deleteMessage(DeleteMessageRequest deleteMessageRequest){
        Member sender = memberRepository.findById(deleteMessageRequest.getSenderId()).orElseThrow(NotFoundByIdException::new);
        MessageRoom messageRoom = messageRoomRepository.findById(deleteMessageRequest.getMessageRoom().getId()).orElseThrow(NotFoundByIdException::new);
        Trade trade = tradeRepository.findByPostId(deleteMessageRequest.getMessageRoom().getPost().getId()).orElseThrow(NotFoundByIdException::new);
        Message message = messageRepository.findById(deleteMessageRequest.getSenderId()).orElseThrow(NotFoundByIdException::new);
        //작성자만 자신의 메시지를 지울 수 있음.
        if(message.getSender().getId().equals(deleteMessageRequest.getSenderId())) {
            messageRepository.delete(message);
        }
        else{
            throw new CanNotDeleteMessage();
        }


        //TODO 7일 뒤 시간 추가
        //거래가 완료되면,
//        if(trade.getTradeStatus() == TradeStatus.TRADED){
//            for(Message message : messageRoom.getMessages()){
//                memberRepository.deleteById(message.getId());
//            }
//        }
        return "DELETE";
    }

    // TODO 철웅 추가, 메세지 해치웠나?
}
