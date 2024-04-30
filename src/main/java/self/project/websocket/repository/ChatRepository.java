package self.project.websocket.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import self.project.websocket.model.Chat;

public interface ChatRepository extends JpaRepository<Chat, Long> {
}
