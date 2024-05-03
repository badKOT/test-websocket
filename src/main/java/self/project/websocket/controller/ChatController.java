package self.project.websocket.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import self.project.websocket.dto.MessageDto;
import self.project.websocket.mapper.DtoMessageMapper;
import self.project.websocket.model.Chat;
import self.project.websocket.service.ChatService;
import self.project.websocket.service.MessageService;

import java.util.List;

@Controller
@RequestMapping("/chats")
@RequiredArgsConstructor
public class ChatController {

    private final ChatService chatService;
    private final MessageService messageService;

    @GetMapping("/")
    @ResponseBody
    public String chatListForUser() throws JsonProcessingException {
//        List<Chat> chatList = chatService.findForUser(username);
        List<Chat> chatList = chatService.findAll();
        // TODO() exclude participants
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(chatList);
    }

    @GetMapping("/{id}")
    public String getChatById(@PathVariable Long id) {
        // TODO()
        return "To be implemented...";
    }

    /**
     * Метод для фетча истории сообщений в чате
     */
    @GetMapping("/start")
    @ResponseBody
    public String init() throws JsonProcessingException {
        List<MessageDto> byChat = messageService
                .findByChat(1L)
                .stream()
                .map(DtoMessageMapper.INSTANCE::toDto)
                .toList();

        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        return mapper.writeValueAsString(byChat);
    }
}
