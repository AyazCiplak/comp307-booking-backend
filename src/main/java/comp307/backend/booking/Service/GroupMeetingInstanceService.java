//Programmed by Henry Niedermayer

package comp307.backend.booking.Service;

import org.springframework.stereotype.Service;

import comp307.backend.account.Object.User;
import comp307.backend.account.Object.UserRepository;
import comp307.backend.booking.Entity.GroupMeetingInstance;
import comp307.backend.booking.Repository.GroupMeetingInstanceRepository;

@Service
public class GroupMeetingInstanceService {
    private final GroupMeetingInstanceRepository groupMeetingInstanceRepository;
    private final UserRepository userRepository;

    public GroupMeetingInstanceService(GroupMeetingInstanceRepository groupMeetingInstanceRepository, UserRepository userRepository) {
        this.groupMeetingInstanceRepository = groupMeetingInstanceRepository;
        this.userRepository = userRepository;
    }


    public GroupMeetingInstance getGroupMeetingInstanceByID(Long groupMeetingInstanceID) {
        return groupMeetingInstanceRepository.findById(groupMeetingInstanceID).orElseThrow(() -> new RuntimeException("GroupMeetingInstance with id " + groupMeetingInstanceID + " not found."));
    }


    public GroupMeetingInstance createGroupMeetingInstance(String ownerEmail, String name, int maxUsers, String inviteToken) {
        User owner = userRepository.findById(ownerEmail).orElseThrow(() -> new RuntimeException("User " + ownerEmail + " not found."));

        GroupMeetingInstance groupMeetingInstance = new GroupMeetingInstance(owner, name, maxUsers, inviteToken); 
        return groupMeetingInstanceRepository.save(groupMeetingInstance);
    }
}
