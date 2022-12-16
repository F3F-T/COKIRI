package f3f.dev1.domain.message.dao;

import f3f.dev1.domain.message.model.Message;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Long> {

    boolean existsById(Long id);
    List<Message> findByMessageRoomId(Long id);
    List<Message> findBySenderId(Long id);
    List<Message> findByReceiverId(Long id);







}
