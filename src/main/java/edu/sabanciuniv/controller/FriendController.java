package edu.sabanciuniv.controller;

import edu.sabanciuniv.model.FriendRequest;
import edu.sabanciuniv.model.User;
import edu.sabanciuniv.repository.UserRepository;
import edu.sabanciuniv.service.FriendService;
import lombok.Data;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/friends")
public class FriendController {

    @Autowired
    private FriendService friendService;

    @Autowired
    private UserRepository userRepository;

    @Getter
    @Data
    public static class FriendRequestDTO {
        private String senderId; // For sending request
        private String receiverId; // For accepting request
        private String status;

    }

    @PostMapping("/add")
    public ResponseEntity<?> sendFriendRequest(@RequestBody FriendRequestDTO request, Authentication authentication){
        String fromUserId = authentication.getName(); 
        String toUserId = request.getReceiverId();

        try {
            FriendRequest friendRequest = friendService.sendFriendRequest(fromUserId, toUserId);
            return ResponseEntity.ok("Friend request sent");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/accept")
    public ResponseEntity<?> acceptFriendRequest(@RequestBody FriendRequestDTO request, Authentication authentication){
        String toUserId = authentication.getName();
        String fromUserId = request.getSenderId();

        try {
            FriendRequest friendRequest = friendService.acceptFriendRequest(fromUserId, toUserId);
            return ResponseEntity.ok("Friend request accepted");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/decline")
    public ResponseEntity<?> declineFriendRequest(@RequestBody FriendRequestDTO request, Authentication authentication){
        String toUserId = authentication.getName();
        String fromUserId = request.getSenderId();



        try {
            FriendRequest friendRequest = friendService.declineFriendRequest_(fromUserId, toUserId);
            return ResponseEntity.ok("Friend request declined");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping()
    public ResponseEntity<?> getFriends(Authentication authentication){
        String userId = authentication.getName();

        try {
            List<User> friends = friendService.getFriends(userId);
            return ResponseEntity.ok(friends);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
