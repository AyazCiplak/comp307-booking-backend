package comp307.backend.booking.Entity;

import jakarta.persistence.*;

import java.sql.Time;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Table(name = "Requests")
public class Request {
    @GeneratedValue
    @Id
    private long id;
    @ManyToOne
    @JoinColumn(name = "requester_email", nullable = false)
    private String requesterEmail;
    @ManyToOne
    @JoinColumn(name = "owner_email", nullable = false)
    @Column
    private String ownerEmail;
    @Column(nullable = false)
    private LocalDateTime requestedStart;
    @Column(nullable = false)
    private LocalDateTime requestedEnd;
    @Column
    private String message;
    @Column(nullable = false)
    private RequestStatus status;
    @Column(nullable = false)
    private long resolvedSlotID;
    @Column(nullable = false)
    private Timestamp createdAt;
    @Column(nullable = false)
    private Timestamp updatedAt;

    public Request(String requesterEmail, String ownerEmail, LocalDateTime requestedStart, LocalDateTime requestedEnd, String message) {
        this.requesterEmail = requesterEmail;
        this.ownerEmail = ownerEmail;
        this.requestedStart = requestedStart;
        this.requestedEnd = requestedEnd;
        this.message = message;
        this.status = RequestStatus.PENDING;
        this.resolvedSlotID = resolvedSlotID; // TODO idk
        this.createdAt = Timestamp.from(Instant.now());
        this.updatedAt = this.createdAt;
    }
    public long getId() {
        return id;
    }

    public String getRequesterEmail() {
        return requesterEmail;
    }

    public String getOwnerEmail() {
        return ownerEmail;
    }

    public LocalDateTime getRequestedStart() {
        return requestedStart;
    }

    public LocalDateTime getRequestedEnd() {
        return requestedEnd;
    }

    public String getMessage() {
        return message;
    }

    public long getRequestedSlotID() {
        return resolvedSlotID;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public Timestamp getUpdatedAt() {
        return updatedAt;
    }
    public void setStatus(boolean accept) {
        if (accept) {
            status = RequestStatus.ACCEPTED;
        } else {
            status = RequestStatus.DECLINED;
        }
    }
    public boolean isPending() {
        return status == RequestStatus.PENDING;
    }

    public enum RequestStatus {
        PENDING,
        ACCEPTED,
        DECLINED
    }
}
