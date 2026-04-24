//Programmed by Henry Niedermayer and Mao Yurun

package comp307.backend.booking.Entity;

import java.time.LocalDateTime;
import comp307.backend.account.Object.Owner;
import jakarta.persistence.*;

//TODO add some sort of indexing
@Entity
@Table(name = "BookingSlots")
public class BookingSlot {
    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Long bookingSlotID;

    @ManyToOne
    @JoinColumn(name = "ownerEmail")
    private Owner owner;

    private LocalDateTime start;
    private LocalDateTime end;
    private boolean activated = false;

    //for JPA
    protected BookingSlot() {}

    public BookingSlot(Owner owner, LocalDateTime start, LocalDateTime end) {
        this.owner = owner;
        this.start = start;
        this.end = end;
    }

    public Long getBookingSlotID() {
        return this.bookingSlotID;
    }

    public Owner getOwner()
    {
        return this.owner;
    }

    public LocalDateTime getStart() {
        return this.start;
    }

    public LocalDateTime getEnd() {
        return this.end;
    }

    public boolean isActivated() {
        return this.activated;
    }

    public void activate() {
        this.activated = true;
    }
}
