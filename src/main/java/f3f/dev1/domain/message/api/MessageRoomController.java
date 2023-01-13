package f3f.dev1.domain.message.api;

import f3f.dev1.domain.message.application.MessageRoomService;
import f3f.dev1.domain.message.application.MessageService;
import f3f.dev1.domain.message.dto.MessageDTO;
import f3f.dev1.domain.message.dto.MessageRoomDTO;
import f3f.dev1.global.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

import static f3f.dev1.domain.message.dto.MessageDTO.*;
import static f3f.dev1.domain.message.dto.MessageRoomDTO.*;
//
//@PostMapping(value = "/post/{postId}/comments")
//public ResponseEntity<CommentInfoDto> createComment(@PathVariable(name = "postId") Long postId, @RequestBody @Valid CreateCommentRequest createCommentRequest) {
//        Long currentMemberId = SecurityUtil.getCurrentMemberId();
//        CommentInfoDto commentInfoDto = commentService.saveComment(createCommentRequest, currentMemberId);
//        return new ResponseEntity<>(commentInfoDto, HttpStatus.CREATED);
//        }
//@RestController
//@Validated
//@RequiredArgsConstructor
//public class MessageRoomController {
//    private final MessageRoomService messageRoomService;
//
//    @PostMapping(value ="/post/{postId}/messageRooms/{messageRoom_id}")
//    public ResponseEntity<MessageRoomSaveRequest> createMessageRoom(@PathVariable(name = "postId") Long postId, @RequestBody @Valid MessageRoomSaveRequest messageRoomSaveRequest){
//        Long currentMemberId = SecurityUtil.getCurrentMemberId();
//        MessageRoomSaveRequest messageRoomSaveDto = messageRoomService.createMessageRoom(messageRoomSaveRequest);
//        return new ResponseEntity<>(messageRoomSaveDto, HttpStatus.CREATED);
//}
//}
