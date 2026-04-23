package comp307.backend.booking.Object;

import comp307.backend.account.Object.Owner;
import comp307.backend.account.Object.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<BookingSlot, BookingPK> {
    List<BookingSlot> findByReservee(User reservee);
}
