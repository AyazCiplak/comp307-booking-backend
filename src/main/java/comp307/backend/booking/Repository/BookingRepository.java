//Programmed by Henry Niedermayer

package comp307.backend.booking.Repository;

import comp307.backend.account.Object.User;
import comp307.backend.booking.Entity.Booking;
import comp307.backend.booking.Entity.BookingSlot;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findByBookingSlot(BookingSlot bookingSlot);
    List<Booking> findByReservee(User reservee);
}
