//Programmed by Henry Niedermayer

package comp307.backend.booking.DTOs;

import java.time.LocalDateTime;

public class CreateGroupMeetingProposalSlot {
    private Long groupMeetingInstanceID;
    private String ownerToken;
    private String title;
    private LocalDateTime startDateTime;
    private LocalDateTime endDateTime;

    public Long getGroupMeetingInstanceID() { return this.groupMeetingInstanceID; }
    public void setGroupMeetingInstanceID(Long groupMeetingInstanceID) { this.groupMeetingInstanceID = groupMeetingInstanceID; }

    public String getOwnerToken() { return ownerToken; }
    public void setOwnerToken(String ownerToken) { this.ownerToken = ownerToken; }

    public String getTitle() { return this.title; }
    public void setTitle(String title) { this.title = title; }

    public LocalDateTime getStartDateTime() { return this.startDateTime; }
    public void setStartDateTime(LocalDateTime startDateTime) { this.startDateTime = startDateTime; }

    public LocalDateTime getEndDateTime() { return this.endDateTime; }
    public void setEndDateTime(LocalDateTime endDateTime) { this.endDateTime = endDateTime; }
}
