package f3f.dev1.domain.message.api;

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

@RestController
@Validated
@RequiredArgsConstructor
public class MessageController {
    private final MessageService messageService;
    @PostMapping(value="/messageRooms/{messageRoomId}")
    public ResponseEntity<MessageInfoDto> createMessage(@PathVariable(name="messageRoomId") Long messageRoomId,@RequestBody @Valid MessageSaveRequest messageSaveRequest){
        Long currentMemberId = SecurityUtil.getCurrentMemberId();
        MessageInfoDto messageInfoDto = messageService.createMessage(messageSaveRequest, currentMemberId);
        return new ResponseEntity<>(messageInfoDto, HttpStatus.CREATED);
    }
}
