package self.project.websocket.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import self.project.websocket.repository.AccountRepository;

@Service
@RequiredArgsConstructor
public class AccountService {
    private final AccountRepository accountRepository;
}
