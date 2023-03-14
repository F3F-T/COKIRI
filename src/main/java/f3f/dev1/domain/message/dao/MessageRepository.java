package f3f.dev1.domain.message.dao;

import f3f.dev1.domain.message.model.Message;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MessageRepository extends JpaRepository<Message, Long> {

    boolean existsById(Long id);
    Optional<Message> findById(Long id);
    List<Message> findByMessageRoomIdOrderByCreateDateDesc(Long id);
    List<Message> findBySenderIdOrderByCreateDateDesc(Long senderId);
    List<Message> findByReceiverIdOrderByCreateDateDesc(Long receiverId);







}
