package org.example.mtspd;

import org.example.mtspd.domain.dto.ChatMessageDto;
import org.example.mtspd.domain.entity.ChatMessage;
import org.example.mtspd.repo.ChatMessageRepository;
import org.example.mtspd.service.MessageFilter;
import org.example.mtspd.service.impl.ChatService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ChatServiceTest {

    @Mock
    private ChatMessageRepository messageRepository;

    @Mock
    private MessageFilter lengthFilter;

    @Mock
    private MessageFilter badWordsFilter;

    private ChatService chatService;

    @BeforeEach
    void setUp() {
        chatService = new ChatService(messageRepository, List.of(lengthFilter, badWordsFilter));
    }

    @Test
    void sendMessage_validData_savesAndReturnsDto() {
        String room = "Europe";
        String nickname = "Alice";
        String text = "Hello from Paris";

        ChatMessage saved = ChatMessage.builder()
                .roomId(room)
                .author(nickname)
                .text(text)
                .sentAt(Instant.now())
                .build();

        when(messageRepository.save(any(ChatMessage.class))).thenReturn(saved);

        ChatMessageDto result = chatService.sendMessage(room, nickname, text);

        // фільтри викликалися
        verify(lengthFilter).check(room, nickname, text);
        verify(badWordsFilter).check(room, nickname, text);
        // репозиторій викликано рівно один раз
        verify(messageRepository).save(any(ChatMessage.class));

        assertEquals(room, result.room());
        assertEquals(nickname, result.author());
        assertEquals(text, result.text());
    }

    @Test
    void sendMessage_emptyText_throwsException() {
        String room = "Europe";
        String nickname = "Bob";
        String text = "   ";

        doThrow(new IllegalArgumentException("Message must not be empty"))
                .when(lengthFilter).check(eq(room), eq(nickname), eq(text));

        assertThrows(IllegalArgumentException.class,
                () -> chatService.sendMessage(room, nickname, text));

        verify(lengthFilter).check(room, nickname, text);
        verify(badWordsFilter, never()).check(any(), any(), any());
        verify(messageRepository, never()).save(any());
    }

    @Test
    void getLastMessages_returnsMessagesInAscendingOrder() {
        String room = "Asia";

        ChatMessage m1 = ChatMessage.builder()
                .roomId(room)
                .author("A")
                .text("Hi")
                .sentAt(Instant.parse("2024-10-10T10:00:00Z"))
                .build();

        ChatMessage m2 = ChatMessage.builder()
                .roomId(room)
                .author("B")
                .text("Hello")
                .sentAt(Instant.parse("2024-10-10T10:01:00Z"))
                .build();

        when(messageRepository.findTopByRoomIdOrderBySentAtDesc(room, 2))
                .thenReturn(List.of(m2, m1));

        List<ChatMessageDto> result = chatService.getLastMessages(room, 2);

        assertEquals(2, result.size());
        assertEquals("Hi", result.get(0).text());
        assertEquals("Hello", result.get(1).text());
    }
}
