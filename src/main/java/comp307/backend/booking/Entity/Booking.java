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
    private BookingSlot bookingSlot;

    @ManyToOne
    @JoinColumn(name = "reserveeEmail", nullable = false)
    private User reservee;

    @Column(nullable = false)
    private LocalDate registeredAt = LocalDate.now();

    //for JPA
    protected Booking() {}

    public Booking(BookingSlot bookingSlot, User reservee) {
        this.bookingSlot = bookingSlot;
        this.reservee = reservee;
    }

    public Long getBookingID() {
        return this.bookingID;
    }

    public BookingSlot getBookingSlot() {
        return this.bookingSlot;
    }

    public User getReservee() {
        return this.reservee;
    }

    public LocalDate getRegisteredAt() {
        return this.registeredAt;
    }
}
