package org.example.mtspd;

import org.example.mtspd.domain.dto.ChatMessageDto;
import org.example.mtspd.repo.ChatMessageRepository;
import org.example.mtspd.service.impl.ChatService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(properties = {
        "spring.data.mongodb.uri=mongodb://localhost:27017/tourist-chat-test"
})
public class ChatControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ChatService chatService;

    @Autowired
    private ChatMessageRepository messageRepository;

    private String lastMessageId;

    @AfterEach
    void tearDown() {
        if (lastMessageId != null) {
            messageRepository.deleteById(lastMessageId);
            lastMessageId = null;
        }
    }

    @Test
    void historyReturnsSavedMessages() throws Exception {
        String room = "Europe";
        String uniqueText = "Hello from Rome " + UUID.randomUUID();

        ChatMessageDto dto = chatService.sendMessage(room, "Alice", uniqueText);
        lastMessageId = dto.id();

        mockMvc.perform(get("/api/chat/rooms/{room}/history", room)
                        .param("limit", "10")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].room").value(room))
                .andExpect(jsonPath("$[0].author").value("Alice"));
    }
}
