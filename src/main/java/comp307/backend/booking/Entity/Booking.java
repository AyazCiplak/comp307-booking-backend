//Programmed by Henry Niedermayer

package comp307.backend.booking.Entity;

import java.time.LocalDate;

import comp307.backend.account.Object.User;
import jakarta.persistence.*;

@Entity
@Table(name = "Bookings", uniqueConstraints = {@UniqueConstraint(columnNames = {"bookingSlotID", "reserveeEmail"})})
public class Booking {
    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Long bookingID;

    @ManyToOne
    @JoinColumn(name = "bookingSlotID", nullable = false)
    private BookingSlot bookingSlotID;

    @ManyToOne
    @JoinColumn(name = "reserveeEmail", nullable = false)
    private User reserveeEmail;

    @Column(nullable = false)
    private LocalDate registeredAt = LocalDate.now();

    //for JPA
    protected Booking() {}

    public Booking(BookingSlot bookingSlotID, User reserveeEmail) {
        this.bookingSlotID = bookingSlotID;
        this.reserveeEmail = reserveeEmail;
    }

    public Long getBookingID() {
        return this.bookingID;
    }

    public BookingSlot getBookingSlotID() {
        return this.bookingSlotID;
    }

    public User getReserveeEmail() {
        return this.reserveeEmail;
    }

    public LocalDate getRegisteredAt() {
        return this.registeredAt;
    }
}
