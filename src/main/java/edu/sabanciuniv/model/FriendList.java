package edu.sabanciuniv.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Set;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "friends")
public class FriendList {
    @Id
    private String id;
    private String username;          // Changed from userId to username
    private Set<String> friends;     // Set of usernames
}