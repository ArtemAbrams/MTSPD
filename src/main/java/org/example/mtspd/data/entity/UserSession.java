package org.example.mtspd.data.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "user_sessions")
public class UserSession {

    @Id
    private String id;
    private String nickname;
    private String roomId;
    private Instant connectedAt;
    private Instant lastActive;

}
