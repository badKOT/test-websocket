package self.project.websocket.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import self.project.websocket.model.Message;

public interface MessageRepository extends JpaRepository<Message, Long> {
}
