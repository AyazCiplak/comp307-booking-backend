//Programmed by Henry Niedermayer

package comp307.backend.booking.ControllerAndDTO;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

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
    public GroupMeetingInstance getGroupMeetingInstance(@RequestParam Long id) {
        return groupMeetingInstanceService.getGroupMeetingInstanceByID(id);   
    }

    
}
