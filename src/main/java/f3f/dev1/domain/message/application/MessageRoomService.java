package f3f.dev1.domain.message.application;

import f3f.dev1.domain.member.dao.MemberRepository;
import f3f.dev1.domain.member.model.Member;
import f3f.dev1.domain.message.dao.MessageRepository;
import f3f.dev1.domain.message.dao.MessageRoomRepository;
import f3f.dev1.domain.message.dto.MessageDTO;
import f3f.dev1.domain.message.dto.MessageRoomDTO;
import f3f.dev1.domain.message.exception.CanNotSendMessageByTradeStatus;
import f3f.dev1.domain.message.exception.NoMessageRoomException;
import f3f.dev1.domain.message.model.Message;
import f3f.dev1.domain.message.model.MessageRoom;
import f3f.dev1.domain.model.TradeStatus;
import f3f.dev1.domain.post.dao.PostRepository;
import f3f.dev1.domain.post.model.Post;
import f3f.dev1.domain.trade.dao.TradeRepository;
import f3f.dev1.domain.trade.model.Trade;
import f3f.dev1.global.error.exception.NotFoundByIdException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static f3f.dev1.domain.message.dto.MessageDTO.*;
import static f3f.dev1.domain.message.dto.MessageRoomDTO.*;
import static f3f.dev1.global.common.constants.ResponseConstants.DELETE;

/*
메시지룸을 만들고 메시지 만들기
C:
R:
U:
D:
    - 거래 완료 후, 7일 뒤에 사라짐.
 */
@Service
@RequiredArgsConstructor
public class MessageRoomService {

    private final MemberRepository memberRepository;
    private final PostRepository postRepository;
    private final TradeRepository tradeRepository;
    private final MessageRoomRepository messageRoomRepository;
    private final MessageRepository messageRepository;
    private final MessageService messageService;

    public Long createMessageRoom(MessageRoomSaveRequest saveRequest){
        Post post = postRepository.findById(saveRequest.getPost().getId()).orElseThrow(NotFoundByIdException::new);
        //파는 사람이 유효한지 확인(메시지를 받는 입장임)
        Member seller = memberRepository.findById(saveRequest.getSeller().getId()).orElseThrow(NotFoundByIdException::new);
        //메시지를 보내는 사람은 물건을 사고자하는 사람.
        Member buyer = memberRepository.findById(saveRequest.getBuyer().getId()).orElseThrow(NotFoundByIdException::new);
        //거래 상태 확인을 위해 포스트에 있는 트레이드 아이디로 가져옴.
        Trade trade = tradeRepository.findByPostId(saveRequest.getPost().getId()).orElseThrow(NotFoundByIdException::new);

        //거래 상태 확인하고 메시지 룸 만들기
        //바로 위에서 초기화하고 트레이드를 쓰는 게 나은지 아니면 그냥 포스트에서 타고 가는게 나은지지(post.getTrade().getTradeStatus()
        if(trade.getTradeStatus() != TradeStatus.TRADABLE){
            throw new CanNotSendMessageByTradeStatus();
        }

        MessageRoom messageRoom = saveRequest.toEntity();
        messageRoomRepository.save(messageRoom);
        //두명의 유저 채팅 리스트에 추가.
        seller.getSellingRooms().add(messageRoom);
        buyer.getBuyingRooms().add(messageRoom);

        return messageRoom.getId();
    }

    //채팅방 클릭할 때, 조회 (채팅창은 멤버에서 관리, 포스트에서 열어볼 수 없음)
    //아이디만 가져와야되나?0 이거 메시지에 들어가야되나? 싹 다 갈아
//    @Transactional(readOnly = true)
//    public List<Message> ReadMessageRoomByMessageRoomId(MessageRoom messageRoom){
//        if(!messageRoomRepository.existsById(messageRoom.getId())){
//            throw new NoMessageRoomException();
//        }
//        //findById
//        return messageRoom.getMessages();
//    }
    @Transactional(readOnly = true)
    public List<Message> ReadMessagesByMessageRoomId(Long id){
       MessageRoom messageRoom = messageRoomRepository.findById(id).orElseThrow(NotFoundByIdException::new);
        return messageRoom.getMessages();
    }

    //포스트에 포함된 메시지룸 수
   @Transactional(readOnly = true)
    public int ReadMessageRoomsByPostId(Long postId){
        Post post = postRepository.findById(postId).orElseThrow(NotFoundByIdException::new);
        return post.getMessageRooms().size();
    }
    //유저 클릭하고 채팅방 조회는 유저에서 할듯?
//    public List<MessageRoom> ReadMessageRoomsBySenderId(Long s){
//        if(!)
//    }

    //지우는 건 없애기
    //TODO 양쪽에서 따로 해야 할듯 메시지와 마찬가지로
//    @Transactional
//    public String deleteMessageRoom(DeleteMessageRoomRequest deleteMessageRoomRequest){
//        MessageRoom messageRoom = messageRoomRepository.findById(deleteMessageRoomRequest.getId()).orElseThrow(NotFoundByIdException::new);
//        //seller는 메시지를 보낸 사람.
//        Member seller = memberRepository.findById(deleteMessageRoomRequest.getSellerId()).orElseThrow(NotFoundByIdException::new);
//        //buyer는 메시지를 받은 사람.
//        Member buyer = memberRepository.findById(deleteMessageRoomRequest.getBuyerId()).orElseThrow(NotFoundByIdException::new);
//        Trade trade = tradeRepository.findByPostId(deleteMessageRoomRequest.getPost().getId()).orElseThrow(NotFoundByIdException::new);
//
//        //TODO 거래 완료 후 일주일 뒤에 지워지도록 수정 -> 레포지토리에서 지우면 안되나?
//        if(trade.getTradeStatus() == TradeStatus.TRADED){
//           if(messageRoom.getMessages().isEmpty()){
//               //TODO 리시버, 센더 따로 해서 지워야되나?-> 테스트코드로 확인해보셈
//               memberRepository.deleteById(messageRoom.getId());
//               //messageRoomRepository.delete(messageRoom);
//           }
//           else{
//               //이거 어떻게 해?
////               messageService.deleteMessage();
//           }
//        }
//    return "DELETE";
//    }



}
