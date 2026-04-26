//Programmed by Henry Niedermayer

package comp307.backend.booking.ControllerAndDTO;

public class CreateBookingRequest {
    private Long bookingSlotID;
    private String reserveeToken;

    public Long getSlotId() {
        return this.bookingSlotID;
    }

    public String getReserveeToken() {
        return this.reserveeToken;
    }
}
