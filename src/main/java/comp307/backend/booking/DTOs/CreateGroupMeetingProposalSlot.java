//Programmed by Henry Niedermayer

package comp307.backend.booking.DTOs;

import java.time.LocalDateTime;

public class CreateGroupMeetingProposalSlot {
    private Long groupMeetingInstanceID;
    private String ownerToken;
    private String title;
    private LocalDateTime startDateTime;
    private LocalDateTime endDateTime;

    public Long getGroupMeetingInstanceID() {
        return this.groupMeetingInstanceID;
    }

    public String getOwnerToken() {
        return ownerToken;
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
