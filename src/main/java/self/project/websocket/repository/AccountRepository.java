package self.project.websocket.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import self.project.websocket.model.Account;

public interface AccountRepository extends JpaRepository<Account, Long> {
}
