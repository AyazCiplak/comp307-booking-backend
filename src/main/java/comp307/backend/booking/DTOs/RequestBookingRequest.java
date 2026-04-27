package comp307.backend.booking.DTOs;

import java.time.LocalDateTime;

public class RequestBookingRequest {
    private String requesterEmail;
    private String ownerEmail;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String message;

    public RequestBookingRequest(String requesterEmail, String ownerEmail, LocalDateTime startTime, LocalDateTime endTime, String message) {
        this.requesterEmail = requesterEmail;
        this.ownerEmail = ownerEmail;
        this.startTime = startTime;
        this.endTime = endTime;
        this.message = message;
    }

    public String getRequesterEmail() {
        return requesterEmail;
    }

    public String getOwnerEmail() {
        return ownerEmail;
    }


    public LocalDateTime getStartTime() {
        return startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public String getMessage() {
        return message;
    }

}
