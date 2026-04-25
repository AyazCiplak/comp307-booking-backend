package comp307.backend.booking.Entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Table;

import java.sql.Time;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.Date;

@Entity
@Table(name = "Requests")
public class Request {
    @GeneratedValue
    private long id;
    @Column
    private String requesterEmail;
    @Column
    private String ownerEmail;
    @Column
    private Date requestedDate;
    @Column
    private Time requestedStart;
    @Column
    private Time requestedEnd;
    @Column
    private String message;
    @Column
    private RequestStatus status;
    @Column
    private long requestedSlotID;
    @Column
    private Timestamp createdAt;
    @Column
    private Timestamp updatedAt;

    public Request(String requesterEmail, String ownerEmail, Date requestedDate, Time requestedStart, Time requestedEnd, String message) {
        this.requesterEmail = requesterEmail;
        this.ownerEmail = ownerEmail;
        this.requestedDate = requestedDate;
        this.requestedStart = requestedStart;
        this.requestedEnd = requestedEnd;
        this.message = message;
        this.status = RequestStatus.PENDING;
        this.requestedSlotID = requestedSlotID; // TODO idk
        this.createdAt = Timestamp.from(Instant.now());
        this.updatedAt = this.createdAt;
    }

    public enum RequestStatus {
        PENDING,
        ACCEPTED,
        DECLINED
    }
}
