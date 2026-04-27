//Programmed by Henry Niedermayer

package comp307.backend.booking.DTOs;

public class CreateBookingRequest {
    private Long bookingSlotID;
    private String reserveeEmail;

    public Long getSlotId() {
        return this.bookingSlotID;
    }

    public String getReserveeEmail() {
        return this.reserveeEmail;
    }
}
