//Programmed by Henry Niedermayer

package comp307.backend.booking.DTOs;

public class CreateBookingRequest {
    private Long bookingSlotID;
    private String reserveeToken;

    // getter name matches field name so Jackson pairs them for both
    // serialisation ("bookingSlotID") and deserialisation ("bookingSlotID").
    public Long getBookingSlotID() {
        return this.bookingSlotID;
    }

    public void setBookingSlotID(Long bookingSlotID) {
        this.bookingSlotID = bookingSlotID;
    }

    public String getReserveeToken() {
        return this.reserveeToken;
    }

    public void setReserveeToken(String reserveeToken) {
        this.reserveeToken = reserveeToken;
    }
}
