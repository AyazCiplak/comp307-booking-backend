//Programmed by Henry Niedermayer

package comp307.backend.booking.Entity;

import comp307.backend.account.Object.User;
import jakarta.persistence.*;

@Entity
@Table(name = "Bookings")
public class Booking {
    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Long bookingID;

    @ManyToOne
    @JoinColumn(name = "bookingSlotID")
    private BookingSlot slotReserved;

    @ManyToOne
    @JoinColumn(name = "reserveeEmail")
    private User reservee;

    //for JPA
    protected Booking() {}

    public Booking(BookingSlot slotReserved, User reservee) {
        this.slotReserved = slotReserved;
        this.reservee = reservee;
    }

    public Long getBookingID() {
        return this.bookingID;
    }

    public BookingSlot getBookingSlotReserved() {
        return this.slotReserved;
    }

    public User getReservee() {
        return this.reservee;
    }
}
