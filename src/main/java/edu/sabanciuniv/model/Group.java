package edu.sabanciuniv.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Document(collection = "groups")
public class Group {
    @Id
    private String id;

    private String name;
    private String createdByUserId;
    private Set<String> memberIds;
}
