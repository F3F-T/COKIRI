package f3f.dev1.domain.message.dao;

import f3f.dev1.domain.message.model.Message;
import f3f.dev1.domain.message.model.MessageRoom;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MessageRoomRepository extends JpaRepository<MessageRoom, Long> {

    boolean existsById(Long id);

    List<MessageRoom> findByPostId(Long id);


}
