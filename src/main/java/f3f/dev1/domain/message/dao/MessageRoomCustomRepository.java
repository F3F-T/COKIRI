package f3f.dev1.domain.message.dao;

import f3f.dev1.domain.member.dto.MemberDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface MessageRoomCustomRepository {
    Page<MemberDTO.GetUserMessageRoom> findUserMessageRoom(Long userId, Pageable pageable);
}
