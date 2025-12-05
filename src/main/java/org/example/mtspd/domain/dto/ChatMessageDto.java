package org.example.mtspd.domain.dto;

import org.example.mtspd.domain.entity.ChatMessage;

import java.time.Instant;

public record ChatMessageDto(String id, String room, String author, String text, Instant sentAt) {

    public static ChatMessageDto from(ChatMessage m) {
        return new ChatMessageDto(m.getId(), m.getRoomId(), m.getAuthor(), m.getText(), m.getSentAt());
    }
}