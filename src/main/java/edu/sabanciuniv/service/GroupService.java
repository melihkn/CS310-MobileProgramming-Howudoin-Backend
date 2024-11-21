package edu.sabanciuniv.service;

import edu.sabanciuniv.model.Group;
import edu.sabanciuniv.model.GroupMessage;
import edu.sabanciuniv.model.Message;
import edu.sabanciuniv.model.User;
import edu.sabanciuniv.repository.GroupMessageRepository;
import edu.sabanciuniv.repository.GroupRepository;
import edu.sabanciuniv.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.*;

@Service
public class GroupService {

    @Autowired
    private GroupRepository groupRepository;

    @Autowired
    private GroupMessageRepository groupMessageRepository;

    @Autowired
    private UserRepository userRepository;

    public Group createGroup(String name, String createdByUserId, Set<String> memberIds) throws Exception {
        // Validate users

        User creatorUser = userRepository.findById(createdByUserId).orElseThrow(() -> new Exception("User not found"));
        for(String memberId : memberIds){
            if(!userRepository.existsById(memberId)){
                throw new Exception("User with ID " + memberId + " does not exist");
            }

            if (memberId.equals(createdByUserId)) {
                throw new Exception("Cannot add creator to group members");
            }

            if (!creatorUser.getFriends().contains(memberId)) {
                throw new Exception("Cannot add non friends to group members");
            }
        }

        // Add the creator to the group if not already
        memberIds.add(createdByUserId);

        Group group = Group.builder()
                .name(name)
                .createdByUserId(createdByUserId)
                .memberIds(memberIds)
                .build();

        return groupRepository.save(group);
    }

    public Group addMemberToGroup(String groupId, String userId, String newMemberId) throws Exception {
        Group group = groupRepository.findById(groupId).orElseThrow(() -> new Exception("Group not found"));
        User adderUser = userRepository.findById(userId).orElseThrow(() -> new Exception("User not found"));
        if(!userRepository.existsById(newMemberId)){
            throw new Exception("User not found");
        }

        if (userId.equals(newMemberId)) {
            throw new Exception("Cannot add creator to group members");
        }

        if (!adderUser.getFriends().contains(newMemberId)) {
            throw new Exception("Cannot add non friends to group members");
        }
        group.getMemberIds().add(newMemberId);
        return groupRepository.save(group);
    }

    public List<User> getGroupMembers(String groupId, String userId) throws Exception {
        Group group = groupRepository.findById(groupId).orElseThrow(() -> new Exception("Group not found"));
        User user = userRepository.findById(userId).orElseThrow(() -> new Exception("User not found"));

        if (!group.getMemberIds().contains(userId)) {
            throw new Exception("User is not a member of the group");
        }
        Set<String> memberIds = group.getMemberIds();
        List<User> members = new ArrayList<>();
        for(String memberId : memberIds){
            userRepository.findById(memberId).ifPresent(members::add);
        }
        return members;
    }

    public GroupMessage sendMessageToGroup(String fromUserId, String groupId, String content) throws Exception {
        // Implement group validation and sending logic
        // For brevity, assuming group exists and user is a member

        User senderUser = userRepository.findById(fromUserId).orElseThrow(() -> new Exception("Sender not found"));

        if (!groupRepository.existsById(groupId)) {
            throw new Exception("Group not found");
        }

        if (!groupRepository.findById(groupId).get().getMemberIds().contains(fromUserId)) {
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
}
