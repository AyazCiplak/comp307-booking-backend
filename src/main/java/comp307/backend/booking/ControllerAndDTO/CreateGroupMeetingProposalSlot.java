package comp307.backend.booking.ControllerAndDTO;

import java.time.LocalDateTime;

public class CreateGroupMeetingProposalSlot {
    private Long groupMeetingInstanceID;
    private String title;
    private LocalDateTime startDateTime;
    private LocalDateTime endDateTime;

    public Long getGroupMeetingInstanceID() {
        return this.groupMeetingInstanceID;
    }

    public String getTitle() {
        return this.title;
    }

    public LocalDateTime getStartDateTime() {
        return this.startDateTime;
    }

    public LocalDateTime getEndDateTime() {
        return this.endDateTime;
    }
}
