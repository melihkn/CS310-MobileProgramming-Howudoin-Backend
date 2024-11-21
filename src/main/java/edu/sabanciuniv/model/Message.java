package edu.sabanciuniv.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Document(collection = "messages")
public class Message {
    @Id
    private String id;

    private String fromUserId;
    private String toUserId; // For one-on-one messages
    private String content;
    private Instant timestamp;
}
