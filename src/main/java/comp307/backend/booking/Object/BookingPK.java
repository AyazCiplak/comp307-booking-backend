package comp307.backend.booking.Object;

import comp307.backend.account.Object.Owner;

import java.io.Serializable;

public class BookingPK implements Serializable {
    private Owner owner;
    private TimeInterval timeInterval;
    public BookingPK(Owner owner, TimeInterval timeInterval) {
        this.owner = owner;
        this.timeInterval = timeInterval;
    }

    @Override
    public boolean equals(Object newBookingPK) {
        if (newBookingPK instanceof BookingPK) {
            return ((BookingPK) newBookingPK).timeInterval.equals(timeInterval) && ((BookingPK) newBookingPK).owner.equals(owner);
        }

        return false;
    }

    public Owner getOwnerEmail() {
        return owner;
    }

    public TimeInterval getTimeInterval() {
        return timeInterval;
    }
}
