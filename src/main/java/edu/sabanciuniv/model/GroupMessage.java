package edu.sabanciuniv.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Document(collection = "groupMessages")
public class GroupMessage {
    @Id
    private String id;

    private String groupId; // To link to the group
    private String fromUserId;
    private String content;
    private Instant timestamp;
}
