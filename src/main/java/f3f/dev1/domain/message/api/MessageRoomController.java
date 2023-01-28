package f3f.dev1.domain.message.api;

import f3f.dev1.domain.comment.dto.CommentDTO;
import f3f.dev1.domain.message.application.MessageRoomService;
import f3f.dev1.domain.message.application.MessageService;
import f3f.dev1.domain.message.dto.MessageDTO;
import f3f.dev1.domain.message.dto.MessageRoomDTO;
import f3f.dev1.global.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import java.util.List;

import static f3f.dev1.domain.message.dto.MessageDTO.*;
import static f3f.dev1.domain.message.dto.MessageRoomDTO.*;

@RestController
@Validated
@RequiredArgsConstructor
public class MessageRoomController {
    private final MessageRoomService messageRoomService;

    //생성 컨트롤러
    @PostMapping(value ="/post/{postId}/messageRooms")
    public ResponseEntity<MessageRoomInfoDto> createMessageRoom(@PathVariable(name = "postId")Long postId, @RequestBody @Valid MessageRoomSaveRequest messageRoomSaveRequest) {
        Long currentMemberId = SecurityUtil.getCurrentMemberId();
        MessageRoomInfoDto messageRoomInfoDto = messageRoomService.createMessageRoom(messageRoomSaveRequest, currentMemberId);
        return new ResponseEntity<>(messageRoomInfoDto, HttpStatus.CREATED);
    }

//    @RequestParam(value="totalMessageRooms", required = false, defaultValue = "") String totalMessageRooms,
//    @RequestParam(value="sellingMessageRooms", required = false, defaultValue = "") String sellingMessageRooms,
//    @RequestParam(value="buyingmessageRooms", required = false, defaultValue ="") String buyingMessageRooms

    // 채팅방 전체 조회 컨트롤러
    @GetMapping(value = "/user/{userId}/totalMessageRooms")
    public ResponseEntity<List<MessageRoomInfoDto>> readTotalMessageRooms(@PathVariable(name="userId")Long userId){
        Long currentMemberId = SecurityUtil.getCurrentMemberId();
        List<MessageRoomInfoDto> totalMessageRoomsDto = messageRoomService.ReadMessageRoomsByUserId(userId, currentMemberId);
        return new ResponseEntity<>(totalMessageRoomsDto, HttpStatus.OK);
    }
    //sellingRoom 조회
    @GetMapping(value = "/user/{userId}/sellingMessageRooms")
    public ResponseEntity<List<SellingRoomInfoDto>> readSellingMessageRooms(@PathVariable(name="userId")Long userId){
        Long currentMemberid = SecurityUtil.getCurrentMemberId();
        List<SellingRoomInfoDto> sellingMessageRoomsDto = messageRoomService.ReadSellingMessageRoomsByUserId(userId, currentMemberid);
        return new ResponseEntity<>(sellingMessageRoomsDto, HttpStatus.OK);
    }
    //buyingRoom 조회
    @GetMapping(value = "/user/{userId}/buyingMessageRooms")
    public ResponseEntity<List<BuyingRoomInfoDto>> readbuyingMessageRooms(@PathVariable(name="userId")Long userId){
        Long currentMemberId = SecurityUtil.getCurrentMemberId();
        List<BuyingRoomInfoDto> buyingMessageRoomsDto = messageRoomService.ReadBuyingMessageRoomsByUserId(userId, currentMemberId);
        return new ResponseEntity<>(buyingMessageRoomsDto, HttpStatus.OK);
    }
    //메시지룸 아이디로 메시지들 조회
    @GetMapping(value = "/messageRooms/{messageRoomId}")
    public ResponseEntity<List<MessageDTO.MessageInfoDto>> readMessagesInMessageRoom(@PathVariable(name="messageRoomId")Long messageRoomId){
        Long currentMemberId = SecurityUtil.getCurrentMemberId();
        List<MessageDTO.MessageInfoDto> messagesDto = messageRoomService.ReadMessagesByMessageRoomId(messageRoomId, currentMemberId);
        return new ResponseEntity<>(messagesDto, HttpStatus.OK);
    }

    @DeleteMapping(value = "/messageRooms/{messageRoomId}")
    public ResponseEntity<String> deleteMessageRoom(@PathVariable(name="messageRoomId")Long messageRoomId,@RequestBody @Valid DeleteMessageRoomRequest deleteMessageRoomRequest){
        Long currentMemberId = SecurityUtil.getCurrentMemberId();
        String deleteMessageRoom = messageRoomService.deleteMessageRoom(deleteMessageRoomRequest, currentMemberId);
        return new ResponseEntity<>(deleteMessageRoom, HttpStatus.OK);
    }

    @DeleteMapping(value = "/messageRooms")
    public ResponseEntity<String> deleteEmptyMessageRoom(@RequestBody MessageRoomDTO.MessageRoomIdDto messageRoomIdDto){
        String deleteEmptyMsgRoom = messageRoomService.deleteEmptyMessageRoom(messageRoomIdDto);
        return new ResponseEntity<>(deleteEmptyMsgRoom, HttpStatus.OK);
    }

}
