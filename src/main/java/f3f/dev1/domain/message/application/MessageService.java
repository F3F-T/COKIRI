package f3f.dev1.domain.message.application;

import f3f.dev1.domain.member.exception.NotAuthorizedException;
import f3f.dev1.domain.message.dao.MessageRepository;
import f3f.dev1.domain.message.dao.MessageRoomRepository;
import f3f.dev1.domain.message.dto.MessageDTO;
import f3f.dev1.domain.message.exception.CanNotDeleteMessage;
import f3f.dev1.domain.message.exception.CanNotSendMessageByTradeStatus;
import f3f.dev1.domain.message.exception.MessageException;
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
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static f3f.dev1.domain.message.dto.MessageDTO.*;
@Slf4j
@Service
@RequiredArgsConstructor
public class MessageService {
    private final MemberRepository memberRepository;
    private final PostRepository postRepository;
    private final MessageRepository messageRepository;
    private final MessageRoomRepository messageRoomRepository;
    private final TradeRepository tradeRepository;

    @Transactional
    public MessageInfoDto createMessage(MessageSaveRequest messageSaveRequest, Long currentMemberId) {

        Member sender = memberRepository.findById(messageSaveRequest.getSenderId()).orElseThrow(UserNotFoundException::new);
        Member receiver = memberRepository.findById(messageSaveRequest.getReceiverId()).orElseThrow(UserNotFoundException::new);
        //포스트가 있는지 확인 포스트 레포지토리에서 findByAuthor쓰고 싶었는데 유저에서 확인하기도 하고 리스트가 와서 존재하는지만 확인하면 receiver는 당연히 자동적으로 연결되니까 확인 안해도 될듯?
        Post post = postRepository.findById(messageSaveRequest.getPostId()).orElseThrow(NotFoundByIdException::new);
        Trade trade = tradeRepository.findByPostId(messageSaveRequest.getPostId()).orElseThrow(NotFoundByIdException::new);

        MessageRoom messageRoom = messageRoomRepository.findById(messageSaveRequest.getMessageRoomId()).orElseThrow(NotFoundByIdException::new);
        if(!sender.getId().equals(currentMemberId)){
            throw new NotAuthorizedException("요청자는 로그인된 유저가 아닙니다.");
        }

        if(messageSaveRequest.getSenderId().equals(messageSaveRequest.getReceiverId())){
            throw new MessageException("본인에게 메시지를 보낼 수 없습니다");
        }
        if(!messageSaveRequest.getSenderId().equals(messageRoom.getBuyer().getId()) && !messageSaveRequest.getSenderId().equals(messageRoom.getSeller().getId())){
            throw new MessageException("메시지룸에 존재하지 않는 사용자입니다.(발신자 오류)");
        }
        if(!messageSaveRequest.getReceiverId().equals(messageRoom.getBuyer().getId()) && !messageSaveRequest.getReceiverId().equals(messageRoom.getSeller().getId())){
            throw new MessageException("메시지룸에 존재하지 않는 사용자입니다.(수신자 오류)");
        }

        //포스트에 메시지를 보낼 수 있는 상태인지 확인. (거래중이거나 완료이면 메시지를 보내지 못함.)
        if(!(trade.getTradeStatus().equals(TradeStatus.TRADABLE))){
            throw new CanNotSendMessageByTradeStatus();
        }
        //TODO 테스트로 메시지 생성시, 센더와 리시버 리스트가 맞게 추가되는지 보기
        Message message = messageSaveRequest.toEntity(sender, receiver, post, messageRoom);
        messageRepository.save(message);
        MessageInfoDto messageInfoDto = message.toMessageInfo();
        messageRoom.getMessages().add(message);
        sender.getSendMessages().add(message);
        receiver.getReceivedMessages().add(message);

        return messageInfoDto;
    }

    //TODO 메시지 삭제는 카톡과 같이 5분 내에 전송시 삭제해야되나?(양방향 삭제 논의)!!!!!
    //자기가 보낸것만 삭제할 수 있음.
    //TODO 채팅방 지우는 형식은 카톡과 동일하게 양쪽 따로 관리하도록 해야할듯.
    @Transactional
    public String deleteMessage(DeleteMessageRequest deleteMessageRequest, Long currentMemberId){
        Member sender = memberRepository.findById(deleteMessageRequest.getSenderId()).orElseThrow(NotFoundByIdException::new);
        MessageRoom messageRoom = messageRoomRepository.findById(deleteMessageRequest.getMessageRoomId()).orElseThrow(NotFoundByIdException::new);
        Trade trade = tradeRepository.findByPostId(messageRoom.getPost().getId()).orElseThrow(NotFoundByIdException::new);
        Message message = messageRepository.findById(deleteMessageRequest.getId()).orElseThrow(NotFoundByIdException::new);


        if(!sender.getId().equals(currentMemberId)){
            throw new NotAuthorizedException("요청자는 로그인된 유저가 아닙니다.");
        }
        //작성자만 자신의 메시지를 지울 수 있음.
        if(message.getSender().getId().equals(deleteMessageRequest.getSenderId())) {
            if(trade.getTradeStatus().equals(TradeStatus.TRADED)){
                throw new CanNotDeleteMessage("거래 완료된 항목의 채팅은 지울 수 없습니다.");
            }
            messageRepository.delete(message);
        }
        else{
            throw new CanNotDeleteMessage("본인만 메시지를 지울 수 있습니다");
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
}
