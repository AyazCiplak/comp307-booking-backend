package comp307.backend.booking.Object;

import comp307.backend.account.Object.Owner;
import comp307.backend.account.Object.User;
import jakarta.persistence.*;

//TODO add some sort of indexing
@Entity
@Table(name = "BookingSlots")
@IdClass(BookingPK.class)
public class BookingSlot {
    @Id
    private Owner owner;
    @Id
    private TimeInterval timeInterval;
    @Column(name = "activated")
    private boolean activated = false;
    @Column(name = "reservee")
    private User reservee;

    public BookingSlot(Owner owner, int beginHour, int beginMinute, int endHour, int endMinute) {
        this.owner = owner;
        this.timeInterval = new TimeInterval(beginHour, beginMinute, endHour, endMinute);
    }

    public void activate() {
        activated = true;
    }
    public boolean isActivated() {
        return activated;
    }

    public void book(User reservee) {
        if (reservee == null) {
            this.reservee = reservee;
        }
    }
    public void unbook(User reservee) {
        if (reservee.equals(this.reservee)) {
            this.reservee = null;
        }
    }
    public User getReservee() {
        return reservee;
    }
    public boolean isAvailable() {
        return activated && reservee == null; // empty booking = available
    }
    public TimeInterval getTimeInterval() {
        return timeInterval;
    }

    @Override
    public boolean equals(Object newSlot) {
        if (newSlot instanceof BookingSlot) {
            return ((BookingSlot) newSlot).timeInterval.equals(this.timeInterval) && ((BookingSlot) newSlot).owner.equals(this.owner);
        }

        return false;
    }
}
