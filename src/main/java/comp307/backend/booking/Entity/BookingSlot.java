//Programmed by Henry Niedermayer and Mao Yurun

package comp307.backend.booking.Entity;

import java.time.LocalDateTime;
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
    @JoinColumn(name = "groupMeetingInstanceID")
    private GroupMeetingInstance groupMeetingInstance;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private BookingSlotType type;

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
    public BookingSlot(User owner, String title, LocalDateTime startDateTime, LocalDateTime endDateTime) {
        this.owner = owner;
        this.type = BookingSlotType.OFFICE_HOURS;
        this.title = title;
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
    }

    //Type 2 proposal constructor
    public BookingSlot(User owner, String title, LocalDateTime startDateTime, LocalDateTime endDateTime, GroupMeetingInstance groupMeetingInstance) {
        this.owner = owner;
        this.type = BookingSlotType.GROUP_PROPOSAL;
        this.title = title;
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
        this.groupMeetingInstance = groupMeetingInstance;
        this.maxUsers = groupMeetingInstance.getMaxUsers();
    }


    public Long getBookingSlotID() {
        return this.bookingSlotID;
    }

    public User getOwner()
    {
        return this.owner;
    }

    public String getTitle() {
        return this.title;
    }

    public BookingSlotType getSlotType() {
        return this.type;
    }

    public LocalDateTime getStartDateTime() {
        return this.startDateTime;
    }

    public LocalDateTime getEndDateTime() {
        return this.endDateTime;
    }

    public BookingSlotStatus getSlotStatus() {
        return this.slotStatus;
    }
    
    public GroupMeetingInstance getGroupMeetingInstance() {
        return this.groupMeetingInstance;
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

    public void markAsSelected() {
        this.type = BookingSlotType.GROUP_SELECTED;
        this.updatedAt = LocalDateTime.now();
    }

    //look into this is needed
    public void setTitle(String title) {
        this.title = title;
        this.updatedAt = LocalDateTime.now();
    }




    public enum BookingSlotType {
        GROUP_PROPOSAL,
        GROUP_SELECTED,
        OFFICE_HOURS
    }

    public enum BookingSlotStatus {
        AVAILABLE, //means there's space
        BOOKED, //no space, only used for Type 2 since Type 3 can never be full
        CANCELLED
    }
}
