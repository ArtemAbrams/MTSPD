package org.example.mtspd.data.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.annotation.Id;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "rooms")
public class ChatRoom {

    @Id
    private String id;
    private String name;
    private String description;
    private Instant createdAt;

}
