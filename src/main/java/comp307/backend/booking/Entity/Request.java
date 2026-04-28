//Programmed by Mao Yurun
package comp307.backend.booking.Entity;

import jakarta.persistence.*;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;

import comp307.backend.account.Object.User;

@Entity
@Table(name = "Requests")
public class Request implements BookingsInterface {
    @GeneratedValue
    @Id
    private long id;

    @ManyToOne
    @JoinColumn(name = "requester", nullable = false)
    private User requester;

    @ManyToOne
    @JoinColumn(name = "owner", nullable = false)
    private User owner;

    @Column(nullable = false)
    private LocalDateTime requestedStart;
    @Column(nullable = false)
    private LocalDateTime requestedEnd;
    @Column
    private String message;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RequestStatus status;
    @Column(nullable = false)
    private Timestamp createdAt;
    @Column(nullable = false)
    private Timestamp updatedAt;
    public Request() {}

    public Request(User requester, User owner, LocalDateTime requestedStart, LocalDateTime requestedEnd, String message) {
        this.requester = requester;
        this.owner = owner;
        this.requestedStart = requestedStart;
        this.requestedEnd = requestedEnd;
        this.message = message;
        this.status = RequestStatus.PENDING;
        this.createdAt = Timestamp.from(Instant.now());
        this.updatedAt = this.createdAt;
    }
    
    public long getId() {
        return id;
    }

    public User getRequester() {
        return requester;
    }

    public User getOwner() {
        return owner;
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

    /** Exposed so Jackson serialises the full status string ("PENDING" / "ACCEPTED" / "DECLINED"). */
    public RequestStatus getStatus() {
        return status;
    }

    public enum RequestStatus {
        PENDING,
        ACCEPTED,
        DECLINED
    }
}
