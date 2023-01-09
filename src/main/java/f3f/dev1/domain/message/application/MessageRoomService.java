package f3f.dev1.domain.message.application;

import f3f.dev1.domain.member.dao.MemberRepository;
import f3f.dev1.domain.member.model.Member;
import f3f.dev1.domain.message.dao.MessageRepository;
import f3f.dev1.domain.message.dao.MessageRoomRepository;
import f3f.dev1.domain.message.dto.MessageDTO;
import f3f.dev1.domain.message.dto.MessageRoomDTO;
import f3f.dev1.domain.message.exception.CanNotMakeMessegeRoom;
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
        Post post = postRepository.findById(saveRequest.getPostId()).orElseThrow(NotFoundByIdException::new);
        //파는 사람이 유효한지 확인(메시지를 받는 입장임)
        Member seller = memberRepository.findById(saveRequest.getSellerId()).orElseThrow(NotFoundByIdException::new);
        //메시지를 보내는 사람은 물건을 사고자하는 사람.
        Member buyer = memberRepository.findById(saveRequest.getBuyerId()).orElseThrow(NotFoundByIdException::new);
        //거래 상태 확인을 위해 포스트에 있는 트레이드 아이디로 가져옴.
        Trade trade = tradeRepository.findByPostId(saveRequest.getPostId()).orElseThrow(NotFoundByIdException::new);

        //포스트 작성자는 메시지를 받는 사람. 즉, 자신한테는 메시지를 남기지 못함.
        if(buyer.getId().equals(post.getAuthor())){
            throw new CanNotMakeMessegeRoom("본인 게시물입니다.");
        }
        //거래 상태 확인하고 메시지 룸 만들기
        //바로 위에서 초기화하고 트레이드를 쓰는 게 나은지 아니면 그냥 포스트에서 타고 가는게 나은지지(post.getTrade().getTradeStatus()
        if(trade.getTradeStatus() != TradeStatus.TRADABLE){
            throw new CanNotSendMessageByTradeStatus();
        }

        MessageRoom messageRoom = saveRequest.toEntity(post, seller, buyer);
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
        return post.getMessageRooms().size(); //쪽지 조회이기 때문에 어차피 다시 들어갔다 나옴.
    }
    //유저 클릭하고 채팅방 조회는 유저에서 할듯?
//    public List<MessageRoom> ReadMessageRoomsBySenderId(Long s){
//        if(!)
//    }

    //지우는 건 없애기
    //TODO 양쪽에서 따로 해야 할듯 메시지와 마찬가지로
    //메시지 룸을 그냥 합치는게 낫지 않을까?
    //내 메시지 방은 우선 내가 관리할 수 있게 함. - 디비에는 남게!
//    @Transactional
//    public String deleteMessageRoom(DeleteMessageRoomRequest deleteMessageRoomRequest){
//        //메시지룸을 유저가 가지고 있는것과 비교해야함. -> 멤버에는 메시지 룸 리스트가 있음.
//        //메시지룸은 어차피 디비상에서는 지워지지 않을거지만 유저와 비교 편리하게 하기 위해 가져옴.
//        //유저가 센딩 메시지룸과 리시브 메시지룸을 구별하면 되기 때문에 그냥 객체로 두는게 나은가? 메시지 레포지토리가 아니라 멤버에서 지워야해서 헷갈림.
//        MessageRoom messageRoom = messageRoomRepository.findById(deleteMessageRoomRequest.getId()).orElseThrow(NotFoundByIdException::new);
//        Member member = memberRepository.findById(deleteMessageRoomRequest.getMemberId()).orElseThrow(NotFoundByIdException::new);
//        Post post = postRepository.findById(deleteMessageRoomRequest.getPostId()).orElseThrow(NotFoundByIdException::new);
//        Trade trade = tradeRepository.findByPostId(deleteMessageRoomRequest.getPostId()).orElseThrow(NotFoundByIdException::new);
//
//        //TODO 거래 완료 후 일주일 뒤에 지워지도록 수정
//        //유저 메시지 방에 있는지 확인해야함.
//        //TODO 그 전에 어디 메시지 룸인지 확인을 해야되나? - 테스트로 확인해보자
//        //포스트 작성자는 seller이기 때문에 메시지를 받는 사람임. -> 우리는 내가 보낸 메시지방, 리스트로 나눠져있지만 프론트는 아니기때문에 우선 이렇게 구현
//        if(post.getAuthor().equals(member.getId())) {
//            for (MessageRoom mr : member.getSellingRooms()) {//객체 비교 보다 아이디 비교가 빠르려나?
//                //selling 방에 지우고자 하는 채팅방이 있으면 메시지 다 지움
//                    if(mr.getId().equals(messageRoom.getId())) {
//                        mr.getMessages().clear();
//                    }
//
//                }
//            }
//        //멤버가 포스트 작성자가 아니면 다 buyer
//        else{
//            for(MessageRoom mr : member.getBuyingRooms()){
//                if(mr.getId().equals(messageRoom.getId())){
//                    mr.getMessages().clear();
//                }
//
//            }
//        }
//    return "DELETE";
//    }



}
