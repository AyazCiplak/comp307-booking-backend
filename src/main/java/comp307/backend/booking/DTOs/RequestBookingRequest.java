package comp307.backend.booking.DTOs;

import java.time.LocalDateTime;

public class RequestBookingRequest {
    private String requesterToken;
    private String ownerEmail;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String message;

    public RequestBookingRequest(String requesterToken, String ownerEmail, LocalDateTime startTime, LocalDateTime endTime, String message) {
        this.requesterToken = requesterToken;
        this.ownerEmail = ownerEmail;
        this.startTime = startTime;
        this.endTime = endTime;
        this.message = message;
    }

    public RequestBookingRequest() {}

    public String getRequesterToken() {
        return requesterToken;
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

    public void setRequesterToken(String requesterToken) { this.requesterToken = requesterToken; }
    public void setOwnerEmail(String ownerEmail) { this.ownerEmail = ownerEmail; }
    public void setStartTime(LocalDateTime startTime) { this.startTime = startTime; }
    public void setEndTime(LocalDateTime endTime) { this.endTime = endTime; }
    public void setMessage(String message) { this.message = message; }
}
