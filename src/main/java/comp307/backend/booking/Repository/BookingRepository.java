//Programmed by Henry Niedermayer

package comp307.backend.booking.Repository;

import comp307.backend.booking.Entity.Booking;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {
    //add whatever is needed
}
