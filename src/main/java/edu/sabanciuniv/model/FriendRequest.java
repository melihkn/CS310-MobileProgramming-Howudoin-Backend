package edu.sabanciuniv.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Document(collection = "friendRequests")
public class FriendRequest {
    @Id
    private String id;
    private String fromUserId;
    private String toUserId;
    private Date requestDate;
    private String status; // e.g., "PENDING", "ACCEPTED", "DECLINED"
}

