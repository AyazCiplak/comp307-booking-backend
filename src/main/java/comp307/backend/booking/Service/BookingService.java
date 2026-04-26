//Programmed by Henry Niedermayer and Mao Yurun

package comp307.backend.booking.Service;

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
    public void createRecurringBookingSlot(String ownerToken, List<LocalDateTime> startDateTimes, List<LocalDateTime> endDateTimes, int weeksToRepeat) {
        User owner = userRepository.findByaccessToken(ownerToken).orElseThrow(() -> new RuntimeException("User " + ownerToken + " not found."));

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
    public BookingSlot createGroupBookingSlot() {
        //todo
        throw new RuntimeException("Not implemented yet");
    }

    public List<BookingSlot> getAllOwnedSlots(String ownerToken) {
        User owner = userRepository.findByaccessToken(ownerToken).orElseThrow(() -> new RuntimeException("User " + ownerToken + " not found."));

        return bookingSlotRepository.findByOwner(owner);
        
    }

    public List<BookingSlot> getAllAvailableOwnedSlots(String ownerEmail, String userToken) {
        userRepository.findByaccessToken(userToken).orElseThrow(() -> new RuntimeException("User " + userToken + " not found."));
        User owner = userRepository.findById(ownerEmail).orElseThrow(() -> new RuntimeException("User " + ownerEmail + " not found."));
        return getAllOwnedSlots(owner.getAccessToken()).stream().filter(bookingSlot -> (bookingSlot.getSlotStatus() == BookingSlot.BookingSlotStatus.AVAILABLE)).toList();
    }

    //maybe add email service, or frontend could default open email with all the people whose bookings got cancelled in which case can return list of emails that should be notified
    public void cancelBookingSlot(String ownerToken, Long bookingSlotId) {
        BookingSlot bookingSlot = bookingSlotRepository.findById(bookingSlotId).orElseThrow(() -> new RuntimeException("Slot " + bookingSlotId + " not found."));
        User owner = userRepository.findByaccessToken(ownerToken).orElseThrow(() -> new RuntimeException("Invalid Token"));

        if (!bookingSlot.getOwner().equals(owner)) {
            throw  new RuntimeException("Invalid access");
        }

        bookingSlot.setSlotStatus(BookingSlot.BookingSlotStatus.CANCELLED);
        bookingSlotRepository.save(bookingSlot);

        List<Booking> bookings = bookingRepository.findByBookingSlot(bookingSlot);
        for (Booking booking : bookings) {
            bookingRepository.delete(booking);
        }
    }




    //BOOKING
    public Booking book(Long bookingSlotId, String reserveeToken) {
        User reservee = userRepository.findByaccessToken(reserveeToken).orElseThrow(() -> new RuntimeException("User " + reserveeToken + " not found."));
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
    public void unbook(Long bookingId, String reserveeToken) {
        User reservee = userRepository.findByaccessToken(reserveeToken).orElseThrow(() -> new RuntimeException("User " + reserveeToken + " not found."));
        Booking booking = bookingRepository.findById(bookingId).orElseThrow(() -> new RuntimeException("Booking " + bookingId + " not found."));

        if (booking.getReservee().equals(reservee)) {
            BookingSlot bookingSlot = booking.getBookingSlot();
            bookingSlot.setSlotStatus(BookingSlot.BookingSlotStatus.AVAILABLE);
            bookingSlotRepository.save(bookingSlot);
            bookingRepository.delete(booking);
        } else {
            throw new RuntimeException("Illegal Access");
        }
    }   
}
