//Programmed by Henry Niedermayer

package comp307.backend.booking.Controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.GetMapping;

import comp307.backend.booking.DTOs.CreateGroupMeetingInstance;
import comp307.backend.booking.Entity.GroupMeetingInstance;
import comp307.backend.booking.Service.GroupMeetingInstanceService;


@RestController
@RequestMapping("api/groupMeetingInstances")
public class GroupMeetingInstanceController {
    private final GroupMeetingInstanceService groupMeetingInstanceService;

    public GroupMeetingInstanceController(GroupMeetingInstanceService groupMeetingInstanceService) {
        this.groupMeetingInstanceService = groupMeetingInstanceService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<GroupMeetingInstance> getGroupMeetingInstance(@PathVariable Long id) {
        return ResponseEntity.ok(groupMeetingInstanceService.getGroupMeetingInstanceByID(id));   
    }

    @GetMapping("/inviteToken")
    public ResponseEntity<GroupMeetingInstance> getGroupMeetingInstanceByInviteToken(@RequestParam String invite) {
        return ResponseEntity.ok(groupMeetingInstanceService.getGroupMeetingInstanceByInviteToken(invite));
    }

    @PostMapping("/create")
    public ResponseEntity<GroupMeetingInstance> createGroupMeetingInstance(@RequestBody CreateGroupMeetingInstance request) {
        GroupMeetingInstance groupMeetingInstance = groupMeetingInstanceService.createGroupMeetingInstance(request.getOwnerToken(), request.getName(), request.getMaxUsers(), request.getInviteToken());
        return ResponseEntity.ok(groupMeetingInstance);
    }

    /**
     * POST /api/groupMeetingInstances/getMyInstances
     * Returns all group meeting instances owned by the authenticated owner. Body = raw token.
     */
    @PostMapping("/getMyInstances")
    public ResponseEntity<java.util.List<GroupMeetingInstance>> getMyInstances(@RequestBody String ownerToken) {
        return ResponseEntity.ok(groupMeetingInstanceService.getMyGroupMeetingInstances(ownerToken));
    }
}
