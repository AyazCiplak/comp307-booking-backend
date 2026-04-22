package comp307.backend.booking.Object;

import comp307.backend.account.Object.User;

public class Booking {
    private User reservee;
    public Booking(User reservee) {
        this.reservee = reservee;
    }
}
