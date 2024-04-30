package self.project.websocket.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import self.project.websocket.model.Message;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Long> {
    List<Message> findByChatId(Long chatId);
}
