package edu.sabanciuniv.repository;

import edu.sabanciuniv.model.Message;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface MessageRepository extends MongoRepository<Message, String> {
    List<Message> findByFromUserIdAndToUserIdOrFromUserIdAndToUserIdOrderByTimestampAsc(
            String fromUserId1, String toUserId1, String fromUserId2, String toUserId2);

}
