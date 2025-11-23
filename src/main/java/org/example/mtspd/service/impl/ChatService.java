package org.example.mtspd.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.mtspd.data.dto.ChatMessageDto;
import org.example.mtspd.data.entity.ChatMessage;
import org.example.mtspd.repo.ChatMessageRepository;
import org.example.mtspd.service.MessageFilter;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatService {

    private final ChatMessageRepository messageRepository;
    private final List<MessageFilter> filters;

    public ChatMessageDto sendMessage(String room, String nickname, String text) {
        filters.forEach(filter -> filter.check(room, nickname, text));

        ChatMessage message = ChatMessage.builder()
                .roomId(room)
                .author(nickname)
                .text(text)
                .sentAt(Instant.now())
                .build();
        ChatMessage saved = messageRepository.save(message);

        return ChatMessageDto.from(saved);
    }

    public List<ChatMessageDto> getLastMessages(String room, int limit) {
        return messageRepository
                .findTopByRoomIdOrderBySentAtDesc(room, limit)
                .stream()
                .sorted(Comparator.comparing(ChatMessage::getSentAt))
                .map(ChatMessageDto::from)
                .toList();
    }

}
