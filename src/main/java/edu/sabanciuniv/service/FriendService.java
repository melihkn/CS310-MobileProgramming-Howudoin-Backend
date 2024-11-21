package edu.sabanciuniv.service;

import edu.sabanciuniv.model.FriendRequest;
import edu.sabanciuniv.model.User;
import edu.sabanciuniv.repository.FriendRequestRepository;
import edu.sabanciuniv.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class FriendService {

    @Autowired
    private FriendRequestRepository friendRequestRepository;

    @Autowired
    private UserRepository userRepository;

    public FriendRequest sendFriendRequest(String senderId, String receiverId) throws Exception {
        if(senderId.equals(receiverId)){
            throw new Exception("Cannot send friend request to yourself");
        }

        Optional<User> fromUser = userRepository.findById(senderId);
        Optional<User> toUser = userRepository.findById(receiverId);

        if(fromUser.isEmpty() || toUser.isEmpty()){
            throw new Exception("User not found");
        }

        // Check if already friends
        if(fromUser.get().getFriends().contains(receiverId)){
            throw new Exception("Users are already friends");
        }

        // Check if a pending request already exists
        Optional<FriendRequest> existingRequest = friendRequestRepository.findByFromUserIdAndToUserIdAndStatus(senderId, receiverId, "PENDING");
        if(existingRequest.isPresent()){
            throw new Exception("Friend request already sent");
        }

        FriendRequest friendRequest = FriendRequest.builder()
                .fromUserId(senderId)
                .toUserId(receiverId)
                .status("PENDING")
                .build();

        return friendRequestRepository.save(friendRequest);
    }

    public FriendRequest acceptFriendRequest(String senderId, String receiverId) throws Exception {
        Optional<FriendRequest> friendRequestOpt = friendRequestRepository.findByFromUserIdAndToUserIdAndStatus(senderId, receiverId, "PENDING");

        if(friendRequestOpt.isEmpty()){
            throw new Exception("Friend request not found");
        }

        FriendRequest friendRequest = friendRequestOpt.get();
        friendRequest.setStatus("ACCEPTED");
        friendRequestRepository.save(friendRequest);

        // Add each other as friends
        User fromUser = userRepository.findById(senderId).orElseThrow(() -> new Exception("Sender user not found"));
        User toUser = userRepository.findById(receiverId).orElseThrow(() -> new Exception("Receiver user not found"));

        fromUser.getFriends().add(receiverId);
        toUser.getFriends().add(senderId);

        userRepository.save(fromUser);
        userRepository.save(toUser);

        return friendRequest;
    }

    public FriendRequest declineFriendRequest_(String senderId, String receiverId) throws Exception {
        Optional<FriendRequest> friendRequestOpt = friendRequestRepository.findByFromUserIdAndToUserIdAndStatus(senderId, receiverId, "PENDING");

        Optional<User> fromUser = userRepository.findById(senderId);
        Optional<User> toUser = userRepository.findById(receiverId);
        if(friendRequestOpt.isEmpty()){
            throw new Exception("Friend request not found");
        }

        FriendRequest friendRequest = friendRequestOpt.get();
        friendRequest.setStatus("DECLINED");
        friendRequestRepository.save(friendRequest);

        return friendRequest;
    }

    public List<User> getFriends(String userId) throws Exception {
        Optional<User> userOpt = userRepository.findById(userId);
        if(userOpt.isEmpty()){
            throw new Exception("User not found");
        }

        Set<String> friendIds = userOpt.get().getFriends();
        List<User> friends = new ArrayList<>();

        for(String friendId : friendIds){
            userRepository.findById(friendId).ifPresent(friends::add);
        }

        return friends;
    }
}
