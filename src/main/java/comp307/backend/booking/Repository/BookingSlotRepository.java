//Programmed by Henry Niedermayer and Mao Yurun

package comp307.backend.booking.Repository;

import comp307.backend.account.Object.Owner;
import comp307.backend.booking.Entity.BookingSlot;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookingSlotRepository extends JpaRepository<BookingSlot, Long> {
    //quick to expand, just add whats needed
    List<BookingSlot> findByOwner(Owner owner);
}
