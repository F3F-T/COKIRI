package f3f.dev1.domain.message.application;

import f3f.dev1.domain.member.dao.MemberRepository;
import f3f.dev1.domain.member.exception.NotAuthorizedException;
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
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

import static f3f.dev1.domain.message.dto.MessageDTO.*;
import static f3f.dev1.domain.message.dto.MessageRoomDTO.*;
import static f3f.dev1.global.common.constants.ResponseConstants.DELETE;

@Service
@Slf4j
@RequiredArgsConstructor
public class MessageRoomService {

    private final MemberRepository memberRepository;
    private final PostRepository postRepository;
    private final TradeRepository tradeRepository;
    private final MessageRoomRepository messageRoomRepository;
    private final MessageRepository messageRepository;
    private final MessageService messageService;

    @Transactional
    public MessageRoomInfoDto createMessageRoom(MessageRoomSaveRequest saveRequest, Long currentMemberId){
        Post post = postRepository.findById(saveRequest.getPostId()).orElseThrow(NotFoundByIdException::new);
        //파는 사람이 유효한지 확인(메시지를 받는 입장임)
        Member seller = memberRepository.findById(post.getAuthor().getId()).orElseThrow(NotFoundByIdException::new);
        //메시지를 보내는 사람은 물건을 사고자하는 사람.
        Member buyer = memberRepository.findById(saveRequest.getBuyerId()).orElseThrow(NotFoundByIdException::new);
        //거래 상태 확인을 위해 포스트에 있는 트레이드 아이디로 가져옴.
        Trade trade = tradeRepository.findByPostId(saveRequest.getPostId()).orElseThrow(NotFoundByIdException::new);

        if(!buyer.getId().equals(currentMemberId)){
            throw new NotAuthorizedException("요청자가 현재 로그인한 유저가 아닙니다");
        }

        //포스트 작성자는 메시지를 받는 사람. 즉, 자신한테는 메시지를 남기지 못함.
        if(buyer.getId().equals(seller.getId())){
            throw new CanNotMakeMessegeRoom("본인 게시물입니다.");
        }
        //거래 상태 확인하고 메시지 룸 만들기
        //바로 위에서 초기화하고 트레이드를 쓰는 게 나은지 아니면 그냥 포스트에서 타고 가는게 나은지지(post.getTrade().getTradeStatus()
        if(trade.getTradeStatus() != TradeStatus.TRADABLE){
            throw new CanNotSendMessageByTradeStatus();
        }
        //메시지룸 중복 생성 로직 추가
        //메시지 룸은 buyer만 만들 수 있다.
        //즉, post에서 senderId만 비교하면 될듯?
        for(MessageRoom msgRoom : post.getMessageRooms()){
            if (msgRoom.getBuyer().getId()==buyer.getId()){
                MessageRoomDTO.MessageRoomInfoDto msgRoomInfo = msgRoom.toMessageRoomInfo();
                return msgRoomInfo;
            }
        }

        MessageRoom messageRoom = saveRequest.toEntity(post, buyer);
        messageRoomRepository.save(messageRoom);
        MessageRoomInfoDto messageRoomInfoDto = messageRoom.toMessageRoomInfo();
//        log.error("messageRoomInfoDto 값 : " + messageRoomInfoDto.isReceiverDelStatus());

        post.getMessageRooms().add(messageRoom);
        //두명의 유저 채팅 리스트에 추가.
        seller.getSellingRooms().add(messageRoom);
        buyer.getBuyingRooms().add(messageRoom);

        return messageRoomInfoDto;
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

    //유저에서 메시지룸들 조회(sellingRoom, buyingRoom 통합)
//    @Transactional(readOnly = true)
//    public List<MessageRoom> ReadMessageRoomsByUserId(Long id){
//        Member member = memberRepository.findById(id).orElseThrow(NotFoundByIdException::new);
//        List<MessageRoom> totalMsgRoom = new ArrayList<>();
//        totalMsgRoom.addAll(member.getBuyingRooms());
//        totalMsgRoom.addAll(member.getSellingRooms());
////        for(MessageRoom buyingMsgroom : member.getBuyingRooms())
////            totalMsgRoom.add(buyingMsgroom);
////        for(MessageRoom sellingMsgroom : member.getSellingRooms())
////            totalMsgRoom.add(sellingMsgroom);
//        return totalMsgRoom;
//    }

    //유저 채팅방 전체 조회
    @Transactional(readOnly = true)
    public List<MessageRoomInfoDto> ReadMessageRoomsByUserId(Long memberId, Long currentMemberId){
        Member member = memberRepository.findById(memberId).orElseThrow(NotFoundByIdException::new);
        if(!memberId.equals(currentMemberId)){
            throw new NotAuthorizedException("로그인한 요청자가 아닙니다!");
        }
        List<MessageRoomInfoDto> totalMsgRoomDtoList = new ArrayList<>();
        for(MessageRoom msgRoom : member.getBuyingRooms()){
            MessageRoomInfoDto msgRoomInfoDto = msgRoom.toMessageRoomInfo();
            totalMsgRoomDtoList.add(msgRoomInfoDto);
        }
        for(MessageRoom msgRoom : member.getSellingRooms()){
            MessageRoomInfoDto msgRoomInfoDto = msgRoom.toMessageRoomInfo();
            totalMsgRoomDtoList.add(msgRoomInfoDto);
        }
//        List<MessageRoom> totalMsgRoom = new ArrayList<>();
//        totalMsgRoom.addAll(member.getBuyingRooms());
//        totalMsgRoom.addAll(member.getSellingRooms());

        return totalMsgRoomDtoList;
    }


//    @Transactional(readOnly = true)
//    public List<MessageRoomInfoWithOneDelStatus> ReadMessageRoomsByUserId(Long memberId, Long currentMemberId){
//        Member member = memberRepository.findById(memberId).orElseThrow(NotFoundByIdException::new);
//        if(!memberId.equals(currentMemberId)){
//            throw new NotAuthorizedException("로그인한 요청자가 아닙니다!");
//        }
//        List<MessageRoomInfoWithOneDelStatus> totalMsgRoomDtoList = new ArrayList<>();
//        for(MessageRoom msgRoom : member.getBuyingRooms()){
//
//            msgRoom.toMessageRoomInfoWithOneDelStatus(member, )
//            totalMsgRoomDtoList.add(msgRoomInfoDto);
//        }
//        for(MessageRoom msgRoom : member.getSellingRooms()){
//            MessageRoomInfoWithOneDelStatus msgRoomInfoDto = msgRoom.toMessageRoomInfo();
//            totalMsgRoomDtoList.add(msgRoomInfoDto);
//        }
////        List<MessageRoom> totalMsgRoom = new ArrayList<>();
////        totalMsgRoom.addAll(member.getBuyingRooms());
////        totalMsgRoom.addAll(member.getSellingRooms());
//
//        return totalMsgRoomDtoList;
//    }


    @Transactional(readOnly = true)
    public String readTotalMessageRoomWithDelStatus(Long memberId, Long currentMemberId){
//        List<MessageRoomDTO.MessageRoomInfoWithOneDelStatus> totalMessageRoomInfoDto = new ArrayList<>();
        List<SellingRoomInfoDto> sellingRoomInfoDtos = ReadSellingMessageRoomsByUserId(memberId, currentMemberId);
        List<BuyingRoomInfoDto> buyingRoomInfoDtos = ReadBuyingMessageRoomsByUserId(memberId, currentMemberId);
//        for(SellingRoomInfoDto mr : sellingRoomInfoDtos){
//
//            totalMessageRoomInfoDto.add(mr);
//        }

        return "READ";
    }

    //유저에서 sellingRoom 조회
    @Transactional(readOnly = true)
    public List<SellingRoomInfoDto> ReadSellingMessageRoomsByUserId(Long memberId, Long currentMemberId){
        Member member = memberRepository.findById(memberId).orElseThrow(NotFoundByIdException::new);
        if(!memberId.equals(currentMemberId)){
            throw new NotAuthorizedException("로그인한 요청자가 아닙니다!");
        }
        List<SellingRoomInfoDto> sellingRoomsInfoDto = new ArrayList<>();
        for(MessageRoom msgRoom : member.getSellingRooms()){
            SellingRoomInfoDto msgRoomInfoDto = msgRoom.toSellingRoomInfo();
            sellingRoomsInfoDto.add(msgRoomInfoDto);
        }
        return sellingRoomsInfoDto;
    }
    //유저에서 BuyingRoom 조회
    @Transactional(readOnly = true)
    public List<BuyingRoomInfoDto> ReadBuyingMessageRoomsByUserId(Long memberId, Long currentMemberId){
        Member member = memberRepository.findById(memberId).orElseThrow(NotFoundByIdException::new);
        if(!memberId.equals(currentMemberId)){
            throw new NotAuthorizedException("로그인한 요청자가 아닙니다!");
        }
        List <BuyingRoomInfoDto> buyingRoomsInfoDto = new ArrayList<>();
        for(MessageRoom msgRoom : member.getBuyingRooms()){
            BuyingRoomInfoDto buyingRoomInfoDto = msgRoom.toBuyingRoomInfo();
            buyingRoomsInfoDto.add(buyingRoomInfoDto);
        }

        return buyingRoomsInfoDto;
    }

    //채팅방에서 "메시지들" 조회
    @Transactional(readOnly = true)
    public List<MessageInfoDto> ReadMessagesByMessageRoomId(Long id, Long currentMemberId){
       MessageRoom messageRoom = messageRoomRepository.findById(id).orElseThrow(NotFoundByIdException::new);
       List <MessageInfoDto> totalMsgInfoDto = new ArrayList<>();
       for(Message msg : messageRoom.getMessages()){
           MessageInfoDto msgInfoDto = msg.toMessageInfo();
           totalMsgInfoDto.add(msgInfoDto);
       }
        return totalMsgInfoDto;
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
    @Transactional
    public String deleteMessageRoom(DeleteMessageRoomRequest deleteMessageRoomRequest, Long currentMemberId){
        //메시지룸을 유저가 가지고 있는것과 비교해야함. -> 멤버에는 메시지 룸 리스트가 있음.
        //메시지룸은 어차피 디비상에서는 지워지지 않을거지만 유저와 비교 편리하게 하기 위해 가져옴.
        //유저가 센딩 메시지룸과 리시브 메시지룸을 구별하면 되기 때문에 그냥 객체로 두는게 나은가? 메시지 레포지토리가 아니라 멤버에서 지워야해서 헷갈림.
        MessageRoom messageRoom = messageRoomRepository.findById(deleteMessageRoomRequest.getId()).orElseThrow(NotFoundByIdException::new);
        //디비에서 안지우니까 사실상 필요 없을 듯
        Member member = memberRepository.findById(deleteMessageRoomRequest.getMemberId()).orElseThrow(NotFoundByIdException::new);
        Post post = postRepository.findById(deleteMessageRoomRequest.getPostId()).orElseThrow(NotFoundByIdException::new);
        Trade trade = tradeRepository.findByPostId(deleteMessageRoomRequest.getPostId()).orElseThrow(NotFoundByIdException::new);
        //        //트레이드 상태 때문에 포스트가 필요한데 우선 둘다 지움.
        if(!member.getId().equals(currentMemberId)){
            throw new NotAuthorizedException("요청자가 현재 로그인한 유저가 아닙니다");
        }
        //TODO 거래 완료 후 일주일 뒤에 지워지도록 수정
        //유저 메시지 방에 있는지 확인해야함.
        //포스트 작성자는 seller이기 때문에 메시지를 받는 사람임. -> 우리는 내가 보낸 메시지방, 리스트로 나눠져있지만 프론트는 아니기때문에 우선 이렇게 구현
        if(post.getAuthor().equals(member.getId())) {

            messageRoom.setReceiverDelStatus(true);
//            for (MessageRoom mr : member.getSellingRooms()) {//객체 비교 보다 아이디 비교가 빠르려나?
//                //selling 방에 지우고자 하는 채팅방이 있으면 메시지 다 지움
//                    if(mr.getId().equals(messageRoom.getId())) {
////                        mr.getMessages().clear();
//                    }
//
//
//
//                }
            }
        //멤버가 포스트 작성자가 아니면 다 buyer
        else{
            messageRoom.setSenderDelStatus(true);
//            for(MessageRoom mr : member.getBuyingRooms()){
//                if(mr.getId().equals(messageRoom.getId())){
//                    mr.getMessages().clear();
//                }
//
//            }

        }
    return "DELETE";
    }

    //currentMemberID는 받지 않음 -> 생성시, 이미 확인을 했고, 뒤로가기를 눌렀을 때 지우는거고 빈방이니까 굳이 없어도 될듯.?
    //멤버에서 메시지룸을 전체 다 불러와서 지우는건 비효율적일듯, 그때그때 삭제하면 그럴 필요도 없을 듯?
    @Transactional
    public String deleteEmptyMessageRoom (MessageRoomIdDto messageRoomIdDto){
        MessageRoom messageRoom = messageRoomRepository.findById(messageRoomIdDto.getId()).orElseThrow(NotFoundByIdException::new);
        if(messageRoom.getMessages().isEmpty()){
            messageRoomRepository.deleteById(messageRoom.getId());
        }
        return "DELETE";
    }



}
