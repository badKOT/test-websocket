package self.project.websocket.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import self.project.websocket.model.Chat;
import self.project.websocket.repository.ChatRepository;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class ChatService {
    private final ChatRepository chatRepository;

    public List<Chat> findAll() {
        return chatRepository.findAll();
    }

    public Chat findById(Long id) {
        return chatRepository.findById(id).orElseThrow(() ->
                new NoSuchElementException("Chat with id " + id + " not found"));
    }
}
