package org.example.mtspd;

import org.example.mtspd.data.entity.ChatMessage;
import org.example.mtspd.repo.ChatMessageRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.TestPropertySource;

import java.time.Instant;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataMongoTest
@TestPropertySource(properties = {
        "spring.data.mongodb.uri=mongodb://localhost:27017/tourist-chat-test"
})
public class ChatMessageRepositoryIntegrationTest {

    @Autowired
    private ChatMessageRepository repository;

    @Autowired
    private MongoTemplate mongoTemplate;

    @AfterEach
    void tearDown() {
        mongoTemplate.getDb().drop();
    }

    @Test
    void saveAndLoadMessages() {
        String room = "World";

        ChatMessage m1 = ChatMessage.builder()
                .roomId(room)
                .author("User1")
                .text("Hi")
                .sentAt(Instant.now().minusSeconds(60))
                .build();

        ChatMessage m2 = ChatMessage.builder()
                .roomId(room)
                .author("User2")
                .text("Hello")
                .sentAt(Instant.now())
                .build();

        repository.save(m1);
        repository.save(m2);

        List<ChatMessage> messages = repository.findTopByRoomIdOrderBySentAtDesc(room, 10);

        assertEquals(2, messages.size());
        assertEquals("Hello", messages.get(0).getText());
    }

}
