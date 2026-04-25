//Programmed by Henry Niedermayer

package comp307.backend.booking.Service;

import comp307.backend.booking.Entity.Request;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import comp307.backend.account.Object.User;
import comp307.backend.account.Object.UserRepository;
import comp307.backend.booking.Entity.Booking;
import comp307.backend.booking.Entity.BookingSlot;
import comp307.backend.booking.Repository.*;

import java.time.LocalDateTime;
import java.util.List;

//Service for Booking and BookingSlot (Type 2 and 3 only)
@Service
public class BookingService {
    private final BookingRepository bookingRepository;
    private final BookingSlotRepository bookingSlotRepository;
    private final UserRepository userRepository;

    public BookingService(BookingRepository bookingRepository, BookingSlotRepository bookingSlotRepository, UserRepository userRepository) {
        this.bookingRepository = bookingRepository;
        this.bookingSlotRepository = bookingSlotRepository;
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
    public BookingSlot createGroupBookingSlot() {
        //todo
    }

    public List<BookingSlot> getAllOwnedSlots(String ownerEmail) {
        User owner = userRepository.findById(ownerEmail).orElseThrow(() -> new RuntimeException("User " + ownerEmail + " not found."));

        return bookingSlotRepository.findByOwner(owner);
        
    }

    public List<BookingSlot> getAllAvailableOwnedSlots(String ownerEmail) {
        return getAllOwnedSlots(ownerEmail).stream().filter(bookingSlot -> (bookingSlot.getSlotStatus() == BookingSlot.BookingSlotStatus.AVAILABLE)).toList();
    }

    //TO DO: delete all the bookings since its now cancelled
    public void cancelBookingSlot(Long bookingSlotId) {
        BookingSlot bookingSlot = bookingSlotRepository.findById(bookingSlotId).orElseThrow(() -> new RuntimeException("Slot " + bookingSlotId + " not found."));
        bookingSlot.setSlotStatus(BookingSlot.BookingSlotStatus.CANCELLED);
        bookingSlotRepository.save(bookingSlot);
    }




    //BOOKING
    public Booking book(Long bookingSlotId, String reserveeEmail) {
        User reservee = userRepository.findById(reserveeEmail).orElseThrow(() -> new RuntimeException("User " + reserveeEmail + " not found."));
        BookingSlot bookingSlot = bookingSlotRepository.findById(bookingSlotId).orElseThrow(() -> new RuntimeException("Slot " + bookingSlotId + " not found."));

        if (!bookingSlot.getSlotStatus().equals(BookingSlot.BookingSlotStatus.AVAILABLE)) {
            throw new RuntimeException("Slot " + bookingSlotId + " is not available.");
        }

        if (bookingSlot.getType().equals(BookingSlot.BookingSlotType.OFFICE_HOURS)) {
            //can never be full so still available.
            return bookingRepository.save(new Booking(bookingSlot, reservee));
        }
        //else it's a group slot and it's limited
        else {
            int currentBookings = bookingRepository.findByBookingSlot(bookingSlot).size();

            if (currentBookings == bookingSlot.getMaxUsers()) {
                //maybe don't throw
                throw new RuntimeException("Slot " + bookingSlotId + " is full.");
            }
            else {
                if (currentBookings + 1 == bookingSlot.getMaxUsers()) {
                    bookingSlot.setSlotStatus(BookingSlot.BookingSlotStatus.BOOKED);
                    bookingSlotRepository.save(bookingSlot);
                }

                return bookingRepository.save(new Booking(bookingSlot, reservee));
            }
        }
    }

    //whether its type 2 or type 3, either way the booking will become available when unbooked because it either had infinite space or now has at least 1 space
    public void unbook(Long bookingId) {
        Booking booking = bookingRepository.findById(bookingId).orElseThrow(() -> new RuntimeException("Booking " + bookingId + " not found."));
        BookingSlot bookingSlot = booking.getBookingSlot();
        bookingSlot.setSlotStatus(BookingSlot.BookingSlotStatus.AVAILABLE);
        bookingSlotRepository.save(bookingSlot);
        bookingRepository.delete(booking);
    }




    /*
    Should be moved to Type 1 Service since it uses different table.

    public void requestBooking(String requesterEmail, String ownerEmail, Date requestedDate, Time requestedStart, Time requestedEnd, String message) {
        requestRepository.save(new Request(requesterEmail, ownerEmail, requestedDate, requestedStart, requestedEnd, message));
    }

    public void acceptRequest(Long requestID) {
        Optional<Request> request = requestRepository.findById(requestID);

        if (request.isPresent()) {

        }
    }
    */
}
