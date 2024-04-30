package self.project.websocket.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import self.project.websocket.repository.MessageRepository;

@Service
@RequiredArgsConstructor
public class MessageService {
    private final MessageRepository messageRepository;
}
