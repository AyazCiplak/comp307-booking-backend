package comp307.backend.booking.ControllerAndDTO;

import java.sql.Date;
import java.sql.Time;

public class RequestBookingRequest {
    private String requesterEmail;
    private String ownerEmail;
    private Date requestDate;
    private Time startTime;
    private Time endTime;
    private String message;

    public RequestBookingRequest(String requesterEmail, String ownerEmail, Date requestDate, Time startTime, Time endTime, String message) {
        this.requesterEmail = requesterEmail;
        this.ownerEmail = ownerEmail;
        this.requestDate = requestDate;
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

    public Date getRequestDate() {
        return requestDate;
    }

    public Time getStartTime() {
        return startTime;
    }

    public Time getEndTime() {
        return endTime;
    }

    public String getMessage() {
        return message;
    }

}
