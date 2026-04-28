//Programmed by Henry Niedermayer

package comp307.backend.booking.Service;

import java.util.Optional;

import org.springframework.stereotype.Service;

import comp307.backend.Exceptions.BadRequestException;
import comp307.backend.Exceptions.NotFoundException;
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
        return groupMeetingInstanceRepository.findById(groupMeetingInstanceID).orElseThrow(() -> new BadRequestException("GroupMeetingInstance with id " + groupMeetingInstanceID + " not found."));
    }


    public GroupMeetingInstance createGroupMeetingInstance(String ownerToken, String name, int maxUsers, String inviteToken) {
        User owner = authService.authenticate(ownerToken);

        GroupMeetingInstance groupMeetingInstance = new GroupMeetingInstance(owner, name, maxUsers, inviteToken); 
        return groupMeetingInstanceRepository.save(groupMeetingInstance);
    }


    /** Returns all group meeting instances created by the authenticated owner. */
    public List<GroupMeetingInstance> getMyGroupMeetingInstances(String ownerToken) {
        User owner = authService.authenticate(ownerToken);
        if (!owner.isOwner()) {
            throw new BadRequestException("You are not an owner");
        }
        // Only return instances that have NOT yet been finalized (i.e., still pending).
        // Finalized instances (where the owner selected a time) appear in "My Booking Slots" instead.
        return groupMeetingInstanceRepository.findByOwner(owner).stream()
                .filter(gm -> !gm.isFinalized())
                .toList();
    }

    //For getting the group meeting instance when a user clicks the invite link
    public GroupMeetingInstance getGroupMeetingInstanceByInviteToken(String inviteToken) {
        Optional<GroupMeetingInstance> groupMeetingInstance = groupMeetingInstanceRepository.findByInviteToken(inviteToken);

        if (groupMeetingInstance.isEmpty()) {
            throw new NotFoundException("Group meeting instance with invite token " + inviteToken + " not found.");
        }

        return groupMeetingInstance.get();
    }
}
