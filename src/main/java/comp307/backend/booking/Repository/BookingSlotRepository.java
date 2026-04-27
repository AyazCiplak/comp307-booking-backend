//Programmed by Henry Niedermayer and Mao Yurun

package comp307.backend.booking.Repository;

import comp307.backend.account.Object.User;
import comp307.backend.booking.Entity.BookingSlot;
import comp307.backend.booking.Entity.GroupMeetingInstance;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookingSlotRepository extends JpaRepository<BookingSlot, Long> {
    List<BookingSlot> findByOwner(User owner);
    List<BookingSlot> findByGroupMeetingInstanceAndSlotType(GroupMeetingInstance groupMeetingInstance, BookingSlot.BookingSlotType bookingSlotType);
}
