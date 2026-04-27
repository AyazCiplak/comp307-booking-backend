//Programmed by Henry Niedermayer

package comp307.backend.booking.Controllers;

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
    public GroupMeetingInstance getGroupMeetingInstance(@PathVariable Long id) {
        return groupMeetingInstanceService.getGroupMeetingInstanceByID(id);   
    }

    @PostMapping("/create")
    public GroupMeetingInstance createGroupMeetingInstance(@RequestBody CreateGroupMeetingInstance request) {
        return groupMeetingInstanceService.createGroupMeetingInstance(request.getOwnerEmail(), request.getName(), request.getMaxUsers(), request.getInviteToken());
    }
}
