package org.example.mtspd.controller;

import lombok.RequiredArgsConstructor;
import org.example.mtspd.domain.dto.ChatInboundMessage;
import org.example.mtspd.domain.dto.ChatMessageDto;
import org.example.mtspd.service.impl.ChatService;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/chat")
public class ChatController {

    private final ChatService chatService;
    private final SimpMessagingTemplate messagingTemplate;

    @GetMapping("/rooms/{room}/history")
    public List<ChatMessageDto> getHistory(@PathVariable String room,
                                           @RequestParam(defaultValue = "20") int limit) {
        return chatService.getLastMessages(room, limit);
    }


    @MessageMapping("/chat/{room}")
    public void handleChatMessage(@DestinationVariable String room, ChatInboundMessage inbound) {
        ChatMessageDto saved = chatService.sendMessage(room, inbound.nickname(), inbound.text());
        messagingTemplate.convertAndSend("/topic/rooms/" + room, saved);
    }


}
