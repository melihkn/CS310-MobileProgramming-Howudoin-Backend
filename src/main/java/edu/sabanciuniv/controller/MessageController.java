package edu.sabanciuniv.controller;

import edu.sabanciuniv.model.Message;
import edu.sabanciuniv.service.MessageService;
import edu.sabanciuniv.service.UserService;
import lombok.Data;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import edu.sabanciuniv.model.Message;

import java.util.List;

@RestController
@RequestMapping("/messages")
public class MessageController {

    @Autowired
    private MessageService messageService;

    @PostMapping("/send")
    public ResponseEntity<?> sendMessage(@RequestBody SendMessageRequest request, Authentication authentication){
        String fromUserId = authentication.getName();
        String toUserId = request.getToUserId();
        String content = request.getContent();

        try{
            Message message = messageService.sendMessageToUser(fromUserId, toUserId, content);
            return ResponseEntity.ok("Message sent");
        } catch(Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/")
    public ResponseEntity<?> getConversation(@RequestBody GetConversationRequest withUserId, Authentication authentication){
        String userId = authentication.getName();
        String toUserId = withUserId.getWithUserId();

        try{
            List<Message> messages = messageService.getConversation(userId, toUserId);
            return ResponseEntity.ok(messages);
        } catch(Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @Getter
    @Data
    public static class SendMessageRequest {
        private String toUserId;
        private String content;
    }

    @Getter
    @Data
    public static class GetConversationRequest {
        private String withUserId;
    }
}
