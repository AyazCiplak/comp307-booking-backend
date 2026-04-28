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
    // Field is named "type" in the entity (getter is getSlotType), so Spring Data must use "Type" here
    List<BookingSlot> findByGroupMeetingInstanceAndType(GroupMeetingInstance groupMeetingInstance, BookingSlot.BookingSlotType type);
}
