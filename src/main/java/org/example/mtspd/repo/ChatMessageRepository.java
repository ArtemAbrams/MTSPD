package org.example.mtspd.repo;

import org.example.mtspd.domain.entity.ChatMessage;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.*;

public interface ChatMessageRepository extends MongoRepository<ChatMessage, String> {

    @Query("{ 'roomId' : ?0 }")
    List<ChatMessage> findTopByRoomIdOrderBySentAtDesc(String roomId, PageRequest pageRequest);

    default List<ChatMessage> findTopByRoomIdOrderBySentAtDesc(String roomId, int limit) {
        return findTopByRoomIdOrderBySentAtDesc(
                roomId,
                PageRequest.of(0, limit, Sort.by(Sort.Direction.DESC, "sentAt"))
        );
    }
}

