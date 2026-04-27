//Programmed by Henry Niedermayer

package comp307.backend.booking.Service;

import org.springframework.stereotype.Service;

import comp307.backend.account.Object.User;
import comp307.backend.account.Object.UserRepository;
import comp307.backend.booking.Entity.Booking;
import comp307.backend.booking.Entity.BookingSlot;
import comp307.backend.booking.Entity.GroupMeetingInstance;
import comp307.backend.booking.Repository.*;
import jakarta.transaction.Transactional;

import java.time.LocalDateTime;
import java.util.List;

//Service for Booking and BookingSlot (Type 2 and 3 only)
@Service
public class BookingService {
    private final BookingRepository bookingRepository;
    private final BookingSlotRepository bookingSlotRepository;
    private final GroupMeetingInstanceRepository groupMeetingInstanceRepository;
    private final UserRepository userRepository;

    public BookingService(BookingRepository bookingRepository, BookingSlotRepository bookingSlotRepository, GroupMeetingInstanceRepository groupMeetingInstanceRepository, UserRepository userRepository) {
        this.bookingRepository = bookingRepository;
        this.bookingSlotRepository = bookingSlotRepository;
        this.groupMeetingInstanceRepository = groupMeetingInstanceRepository;
        this.userRepository = userRepository;
    }



    //BOOKING SLOT
    //Type 3
    //startDateTimes/endDateTimes are not to be confused with listing all the weeks. Instead, they list the first weeks time slots. For example Monday Jan 1st 2/3 pm and Tuesday Jan 2nd 3/4 pm. 
    //From this we use weeksToRepeat to loop and make booking slots for the future weeks.
    public void createRecurringBookingSlot(String ownerEmail, List<LocalDateTime> startDateTimes, List<LocalDateTime> endDateTimes, int weeksToRepeat) {
        User owner = userRepository.findById(ownerEmail).orElseThrow(() -> new RuntimeException("User " + ownerEmail + " not found."));

        if (startDateTimes.size() != endDateTimes.size()) {
            throw new RuntimeException("Size of start date times and end date times do not match.");
        }

        for (int i = 0; i < weeksToRepeat; i++) {
            for (int j = 0; j < startDateTimes.size(); j++) {
                LocalDateTime startDateTime = startDateTimes.get(j).plusWeeks(i);
                LocalDateTime endDateTime = endDateTimes.get(j).plusWeeks(i);

                bookingSlotRepository.save(new BookingSlot(owner, startDateTime, endDateTime));
            }
        }
    }

    //Type 2
    //Already enforced on front end that there can't be overlapping groups or end date earlier than start date etc.
    public BookingSlot createGroupMeetingBookingProposalSlot(Long groupMeetingInstanceID, String title, LocalDateTime startDateTime, LocalDateTime endDateTime) {
        GroupMeetingInstance groupMeetingInstance = groupMeetingInstanceRepository.findById(groupMeetingInstanceID).orElseThrow(() -> new RuntimeException("Group meeting instance " + groupMeetingInstanceID + " not found."));

        BookingSlot bookingSlot = new BookingSlot(groupMeetingInstance.getOwner(), title, startDateTime, endDateTime, groupMeetingInstance);
        return bookingSlotRepository.save(bookingSlot);
        
    }

    //Type 2
    //Selects one proposal slot, deletes the others and their bookings (availability markings)
    @Transactional
    public void selectGroupMeetingProposalSlot(Long bookingSlotId) {
        BookingSlot selectedBookingSlot = bookingSlotRepository.findById(bookingSlotId).orElseThrow(() -> new RuntimeException("Slot " + bookingSlotId + " not found."));

        if (selectedBookingSlot.getSlotType() != BookingSlot.BookingSlotType.GROUP_PROPOSAL) {
            throw new IllegalArgumentException("Slot " + bookingSlotId + " is not a group meeting proposal slot.");
        }

        if (selectedBookingSlot.getSlotStatus() == BookingSlot.BookingSlotStatus.CANCELLED) {
            throw new IllegalArgumentException("Slot " + bookingSlotId + " was cancelled.");
        }

        selectedBookingSlot.markAsSelected();
        bookingSlotRepository.save(selectedBookingSlot);

        List<BookingSlot> otherUnselectedProposals = bookingSlotRepository.findByGroupMeetingInstanceAndSlotType(selectedBookingSlot.getGroupMeetingInstance(), BookingSlot.BookingSlotType.GROUP_PROPOSAL);

        for (BookingSlot otherProposal : otherUnselectedProposals) {
            List<Booking> unusedBookings = bookingRepository.findByBookingSlot(otherProposal);

            for (Booking booking : unusedBookings) {
                bookingRepository.delete(booking);
            }

            bookingSlotRepository.delete(otherProposal);
        }
    }

    public List<BookingSlot> getAllOwnedSlots(String ownerEmail) {
        User owner = userRepository.findById(ownerEmail).orElseThrow(() -> new IllegalArgumentException("User " + ownerEmail + " not found."));

        return bookingSlotRepository.findByOwner(owner);
        
    }

    public List<BookingSlot> getAllAvailableOwnedSlots(String ownerEmail) {
        return getAllOwnedSlots(ownerEmail).stream().filter(bookingSlot -> (bookingSlot.getSlotStatus() == BookingSlot.BookingSlotStatus.AVAILABLE)).toList();
    }

    //maybe add email service, or frontend could default open email with all the people whose bookings got cancelled in which case can return list of emails that should be notified
    public void cancelBookingSlot(Long bookingSlotId) {
        BookingSlot bookingSlot = bookingSlotRepository.findById(bookingSlotId).orElseThrow(() -> new IllegalArgumentException("Slot " + bookingSlotId + " not found."));
        bookingSlot.setSlotStatus(BookingSlot.BookingSlotStatus.CANCELLED);
        bookingSlotRepository.save(bookingSlot);

        List<Booking> bookings = bookingRepository.findByBookingSlot(bookingSlot);
        for (Booking booking : bookings) {
            bookingRepository.delete(booking);
        }
    }




    //BOOKING
    //For type 3
    public Booking book(Long bookingSlotId, String reserveeEmail) {
        User reservee = userRepository.findById(reserveeEmail).orElseThrow(() -> new RuntimeException("User " + reserveeEmail + " not found."));
        BookingSlot bookingSlot = bookingSlotRepository.findById(bookingSlotId).orElseThrow(() -> new RuntimeException("Slot " + bookingSlotId + " not found."));

        if (!bookingSlot.getSlotStatus().equals(BookingSlot.BookingSlotStatus.AVAILABLE)) {
            throw new IllegalArgumentException("Slot " + bookingSlotId + " is not available.");
        }

        if (bookingSlot.getSlotType() != BookingSlot.BookingSlotType.OFFICE_HOURS) {
            throw new IllegalArgumentException("Slot " + bookingSlotId + " is not an office hours slot.");
        }

        //can never be full so still available.
        return bookingRepository.save(new Booking(bookingSlot, reservee));
        
    }

    //Type 2
    //Might need another function/modify this one to add the ability to join the Selected group meeting proposal that they haven't accepted yet.
    public Booking markAvailabilityForProposal(Long bookingSlotId, String reserveeEmail) {
        User reservee = userRepository.findById(reserveeEmail).orElseThrow(() -> new RuntimeException("User " + reserveeEmail + " not found."));
        BookingSlot bookingSlot = bookingSlotRepository.findById(bookingSlotId).orElseThrow(() -> new RuntimeException("Slot " + bookingSlotId + " not found."));

        if (bookingSlot.getSlotType() != BookingSlot.BookingSlotType.GROUP_PROPOSAL) {
            throw new IllegalArgumentException("Slot " + bookingSlotId + " is not a pending proposal slot.");
        }

        if (bookingSlot.getSlotStatus() != BookingSlot.BookingSlotStatus.AVAILABLE) {
            throw new IllegalArgumentException("Slot " + bookingSlotId + " is not available for marking availability.");
        }

        int currentBookingsCount = bookingRepository.findByBookingSlot(bookingSlot).size();
        if (currentBookingsCount + 1 == bookingSlot.getMaxUsers()) {
            bookingSlot.setSlotStatus(BookingSlot.BookingSlotStatus.BOOKED);
            bookingSlotRepository.save(bookingSlot);
        }

        return bookingRepository.save(new Booking(bookingSlot, reservee));
    }

    //whether its type 2 or type 3, either way the booking will become available when unbooked because it either had infinite space or now has at least 1 space
    public void unbook(Long bookingId) {
        Booking booking = bookingRepository.findById(bookingId).orElseThrow(() -> new RuntimeException("Booking " + bookingId + " not found."));
        BookingSlot bookingSlot = booking.getBookingSlot();

        if (bookingSlot.getSlotStatus() == BookingSlot.BookingSlotStatus.CANCELLED) {
            throw new IllegalArgumentException("Slot " + bookingSlot.getBookingSlotID() + " is cancelled, should not be calling this function.");
        }

        bookingSlot.setSlotStatus(BookingSlot.BookingSlotStatus.AVAILABLE);
        bookingSlotRepository.save(bookingSlot);
        bookingRepository.delete(booking);
    }
}
