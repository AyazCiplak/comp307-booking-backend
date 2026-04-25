//Programmed by Henry Niedermayer and Mao Yurun

package comp307.backend.booking.Entity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import comp307.backend.account.Object.User;
import jakarta.persistence.*;

@Entity
@Table(name = "BookingSlots")
public class BookingSlot {
    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Long bookingSlotID;

    @ManyToOne
    @JoinColumn(name = "ownerEmail", nullable = false)
    private User owner;

    //Only for Type 2 (group meeting)
    @ManyToOne
    @JoinColumn(name = "sequenceID")
    private MeetingSequence meetingSequence;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private BookingSlotType type;

    //look into how this should be set, maybe at constructor in which case not nullable or maybe by a setter
    private String title;

    @Column(nullable = false)
    private LocalDateTime startDateTime;
    @Column(nullable = false)
    private LocalDateTime endDateTime;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private BookingSlotStatus slotStatus = BookingSlotStatus.AVAILABLE;

    //Only for Type 2 (group meeting)
    private int maxUsers;

    @Column(nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();
    @Column(nullable = false)
    private LocalDateTime updatedAt = LocalDateTime.now();

    //for JPA
    protected BookingSlot() {}

    //Type 3 constructor
    public BookingSlot(User owner, LocalDateTime startTime, LocalDateTime endTime) {
        this.owner = owner;
        this.type = BookingSlotType.OFFICE_HOURS;
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
    }

    //Type 2 constructor
    public BookingSlot(User owner, LocalDateTime startTime, LocalDateTime endTime, MeetingSequence meetingSequence) {
        this.owner = owner;
        this.type = BookingSlotType.GROUP;
        this.startDateTime = startTime;
        this.endDateTime = endTime;
        this.meetingSequence = meetingSequence;
        this.maxUsers = meetingSequence.getMaxUsers();
    }


    public Long getBookingSlotID() {
        return this.bookingSlotID;
    }

    public User getOwner()
    {
        return this.owner;
    }

    public BookingSlotType getType() {
        return this.type;
    }

    public LocalDateTime getStart() {
        return this.startDateTime;
    }

    public LocalDateTime getEndDateTime() {
        return this.endDateTime;
    }

    public BookingSlotStatus getSlotStatus() {
        return this.slotStatus;
    }
    
    public MeetingSequence getSequence() {
        return this.meetingSequence;
    }

     public int getMaxUsers() {
        return this.maxUsers;
    }

     public LocalDateTime getCreatedAt() {
        return this.createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return this.updatedAt;
    }

     public void setSlotStatus(BookingSlotStatus newStatus) {
        this.slotStatus = newStatus;
        this.updatedAt = LocalDateTime.now();
    }




    public enum BookingSlotType {
        GROUP,
        OFFICE_HOURS
    }

    public enum BookingSlotStatus {
        AVAILABLE,
        BOOKED,
        CANCELLED
    }
}
