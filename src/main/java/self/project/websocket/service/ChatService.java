package self.project.websocket.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import self.project.websocket.repository.ChatRepository;

@Service
@RequiredArgsConstructor
public class ChatService {
    private final ChatRepository chatRepository;
}
