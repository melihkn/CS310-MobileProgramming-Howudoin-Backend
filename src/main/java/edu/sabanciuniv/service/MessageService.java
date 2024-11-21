package edu.sabanciuniv.service;

import edu.sabanciuniv.model.Group;
import edu.sabanciuniv.model.GroupMessage;
import edu.sabanciuniv.model.Message;
import edu.sabanciuniv.model.User;
import edu.sabanciuniv.repository.GroupMessageRepository;
import edu.sabanciuniv.repository.GroupRepository;
import edu.sabanciuniv.repository.MessageRepository;
import edu.sabanciuniv.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
public class MessageService {

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private GroupMessageRepository groupMessageRepository;

    @Autowired
    private GroupRepository groupRepository;

    public Message sendMessageToUser(String fromUserId, String toUserId, String content) throws Exception {
        // Check if both users exist
        User fromUser = userRepository.findById(fromUserId).orElseThrow(() -> new Exception("Sender not found"));
        User toUser = userRepository.findById(toUserId).orElseThrow(() -> new Exception("Recipient not found"));

        // Check if id's are the same
        if (fromUserId.equals(toUserId)) {
            throw new Exception("Cannot send message to yourself");
        }

        // Check if they are friends
        if(!fromUser.getFriends().contains(toUserId)){
            throw new Exception("Users are not friends");
        }

        Message message = Message.builder()
                .fromUserId(fromUserId)
                .toUserId(toUserId)
                .content(content)
                .timestamp(Instant.now())
                .build();

        return messageRepository.save(message);
    }

    public List<Message> getConversation(String userId1, String userId2) throws Exception {
        User fromUser = userRepository.findById(userId1).orElseThrow(() -> new Exception("Sender not found"));
        User toUser = userRepository.findById(userId2).orElseThrow(() -> new Exception("Recipient not found"));

        // Check if id's are the same
        if (userId1.equals(userId2)) {
            throw new Exception("Cannot send message to yourself");
        }

        // Check if they are friends
        if(!fromUser.getFriends().contains(userId2)){
            throw new Exception("Users are not friends");
        }

        return messageRepository.findByFromUserIdAndToUserIdOrFromUserIdAndToUserIdOrderByTimestampAsc(
                userId1, userId2, userId2, userId1);
    }

    public GroupMessage sendMessageToGroup(String fromUserId, String groupId, String content) throws Exception {
        // Implement group validation and sending logic
        // For brevity, assuming group exists and user is a member

        Group group = groupRepository.findById(groupId).orElseThrow(() -> new Exception("Group not found"));
        User user = userRepository.findById(fromUserId).orElseThrow(() -> new Exception("User not found"));

        if (!group.getMemberIds().contains(fromUserId)) {
            throw new Exception("User is not a member of the group");
        }

        GroupMessage message = GroupMessage.builder()
                .fromUserId(fromUserId)
                .groupId(groupId)
                .content(content)
                .timestamp(Instant.now())
                .build();

        return groupMessageRepository.save(message);
    }

    public List<GroupMessage> getGroupMessages(String groupId, String userId) throws Exception {

        Group group = groupRepository.findById(groupId).orElseThrow(() -> new Exception("Group not found"));
        User user = userRepository.findById(userId).orElseThrow(() -> new Exception("User not found"));

        if (!group.getMemberIds().contains(userId)) {
            throw new Exception("User is not a member of the group");
        }

        return groupMessageRepository.findByGroupIdOrderByTimestampAsc(groupId);
    }
}
