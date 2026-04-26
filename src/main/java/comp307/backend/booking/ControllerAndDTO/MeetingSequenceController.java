//Programmed by Henry Niedermayer

package comp307.backend.booking.ControllerAndDTO;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import comp307.backend.booking.Entity.MeetingSequence;
import comp307.backend.booking.Service.MeetingSequenceService;


@RestController
@RequestMapping("api/meetingSequences")
public class MeetingSequenceController {
    private final MeetingSequenceService meetingSequenceService;

    public MeetingSequenceController(MeetingSequenceService meetingSequenceService) {
        this.meetingSequenceService = meetingSequenceService;
    }

    @GetMapping("/{id}")
    public MeetingSequence getMeetingSequence(@RequestParam Long id) {
        return meetingSequenceService.getMeetingSequenceByID(id);   
    }

    
}
