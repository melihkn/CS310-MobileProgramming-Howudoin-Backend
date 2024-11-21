package edu.sabanciuniv.controller;

import edu.sabanciuniv.model.Group;
import edu.sabanciuniv.model.Message;
import edu.sabanciuniv.model.User;
import edu.sabanciuniv.model.GroupMessage;
import edu.sabanciuniv.service.GroupService;
import edu.sabanciuniv.service.MessageService;
import edu.sabanciuniv.service.UserService;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/groups")
public class GroupController {

    @Autowired
    private GroupService groupService;

    @Autowired
    private MessageService messageService;


    @Autowired
    private UserService userService;

    @PostMapping("/create")
    public ResponseEntity<?> createGroup(@RequestBody CreateGroupRequest request, Authentication authentication){
        String createdByUserId = authentication.getName();
        String groupName = request.getName();
        Set<String> memberIds = request.getMemberIds();

        try{
            Group group = groupService.createGroup(groupName, createdByUserId, memberIds);
            return ResponseEntity.ok("Group created with ID: " + group.getId());
        } catch(Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/{groupId}/add-member")
    public ResponseEntity<?> addMember(@PathVariable String groupId, @RequestBody AddMemberRequest request, Authentication authentication){
        String userId = authentication.getName();
        String newMemberId = request.getUserId();

        try{
            Group group = groupService.addMemberToGroup(groupId, userId, newMemberId);
            return ResponseEntity.ok("Member added to group");
        } catch(Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/{groupId}/send")
    public ResponseEntity<?> sendGroupMessage(@PathVariable String groupId, @RequestBody SendGroupMessageRequest request, Authentication authentication){
        String fromUserId = authentication.getName();
        String content = request.getContent();

        try{
            GroupMessage message = groupService.sendMessageToGroup(fromUserId, groupId, content);
            return ResponseEntity.ok("Message sent to group");
        } catch(Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/{groupId}/messages")
    public ResponseEntity<?> getGroupMessages(@PathVariable String groupId, Authentication authentication){

        String userId = authentication.getName();

        try{
            List<GroupMessage> messages = messageService.getGroupMessages(groupId, userId);
            return ResponseEntity.ok(messages);
        } catch(Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/{groupId}/members")
    public ResponseEntity<?> getGroupMembers(@PathVariable String groupId, Authentication authentication){
        try{
            String userId = authentication.getName();
            List<User> members = groupService.getGroupMembers(groupId, userId);
            return ResponseEntity.ok(members);
        } catch(Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @Data
    public static class CreateGroupRequest {
        private String name;
        private Set<String> memberIds;
    }

    @Data
    public static class AddMemberRequest {
        private String userId;
    }

    @Data
    public static class SendGroupMessageRequest {
        private String content;
    }
}
