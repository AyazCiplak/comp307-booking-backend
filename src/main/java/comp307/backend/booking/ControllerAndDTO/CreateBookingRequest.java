//Programmed by Henry Niedermayer

package comp307.backend.booking.ControllerAndDTO;

import comp307.backend.account.Object.User;

public class CreateBookingRequest {
    private Long bookingSlotID;
    //might have to be email instead of the entire user object
    private User reservee;

    public Long getSlotId() {
        return this.bookingSlotID;
    }

    public User getReservee() {
        return this.reservee;
    }
}
