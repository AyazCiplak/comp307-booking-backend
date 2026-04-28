//Programmed by Henry Niedermayer

package comp307.backend.booking.Service;

import java.util.List;

import org.springframework.stereotype.Service;

import comp307.backend.account.Object.User;
import comp307.backend.account.auth.AuthService;
import comp307.backend.booking.Entity.GroupMeetingInstance;
import comp307.backend.booking.Repository.GroupMeetingInstanceRepository;

@Service
public class GroupMeetingInstanceService {
    private final AuthService authService;
    private final GroupMeetingInstanceRepository groupMeetingInstanceRepository;

    public GroupMeetingInstanceService(AuthService authService, GroupMeetingInstanceRepository groupMeetingInstanceRepository) {
        this.authService = authService;
        this.groupMeetingInstanceRepository = groupMeetingInstanceRepository;
    }


    public GroupMeetingInstance getGroupMeetingInstanceByID(Long groupMeetingInstanceID) {
        return groupMeetingInstanceRepository.findById(groupMeetingInstanceID).orElseThrow(() -> new RuntimeException("GroupMeetingInstance with id " + groupMeetingInstanceID + " not found."));
    }


    public GroupMeetingInstance createGroupMeetingInstance(String ownerToken, String name, int maxUsers, String inviteToken) {
        User owner = authService.authenticate(ownerToken);

        GroupMeetingInstance groupMeetingInstance = new GroupMeetingInstance(owner, name, maxUsers, inviteToken); 
        return groupMeetingInstanceRepository.save(groupMeetingInstance);
    }


    //For getting the group meeting instance when a user clicks the invite link
    public GroupMeetingInstance getGroupMeetingInstanceByInviteToken(String inviteToken) {
        List<GroupMeetingInstance> groupMeetingInstances = groupMeetingInstanceRepository.findByInviteToken(inviteToken);

        if (groupMeetingInstances.size() != 1) {
            throw new RuntimeException("Should be exactly one group meeting instance with invite token " + inviteToken + " but found " + groupMeetingInstances.size());
        }

        return groupMeetingInstances.get(0);
    }
}
