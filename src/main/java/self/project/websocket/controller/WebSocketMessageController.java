package self.project.websocket.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;
import self.project.websocket.dto.MessageDto;
import self.project.websocket.mapper.DtoAccountMapper;
import self.project.websocket.model.Account;
import self.project.websocket.model.Chat;
import self.project.websocket.service.AccountService;
import self.project.websocket.service.ChatService;
import self.project.websocket.service.MessageService;

@Controller
@RequiredArgsConstructor
public class WebSocketMessageController {

    private final MessageService messageService;
    private final AccountService accountService;
    private final ChatService chatService;

    @MessageMapping("/chat.sendMessage")
    @SendTo("/topic/public")
    public MessageDto sendMessage(@Payload MessageDto messageDto) {
        System.out.println("Got the request to send the message: " + messageDto);

        Account account = accountService.findByUsername(messageDto.getSender());
        Chat chat = chatService.findById(1L); // TODO() proper chat mapping
        messageService.save(messageDto, account, chat);

        return messageDto;
    }

    @MessageMapping("/chat.addUser")
    @SendTo("/topic/public")
    public MessageDto addUser(@Payload MessageDto messageDto, SimpMessageHeaderAccessor headerAccessor) {
        System.out.println("Got the request to add user: " + messageDto);

        Account account = DtoAccountMapper.INSTANCE.toEntity(messageDto);
        if (accountService.findByUsername(messageDto.getSender()) == null) {
            accountService.save(account);
        }

        // add username to web socket session
        headerAccessor.getSessionAttributes().put("username", messageDto.getSender());
        return messageDto;
    }
}
