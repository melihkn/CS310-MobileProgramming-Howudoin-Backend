package edu.sabanciuniv.repository;

import edu.sabanciuniv.model.FriendRequest;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface FriendRequestRepository extends MongoRepository<FriendRequest, String> {
    List<FriendRequest> findByFromUserIdAndToUserIdAndStatus(String senderUsername, String receiverUsername);
    Optional<FriendRequest> findByFromUserIdAndToUserIdAndStatus(String senderUsername, String receiverUsername, String status);
}
