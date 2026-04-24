package comp307.backend.booking.Service;

import org.springframework.stereotype.Service;

import comp307.backend.account.Object.User;
import comp307.backend.booking.Entity.Booking;
import comp307.backend.booking.Entity.BookingSlot;
import comp307.backend.booking.Repository.*;

@Service
public class BookingService {
    private final BookingRepository bookingRepository;
    private final BookingSlotRepository bookingSlotRepository;

    public BookingService(BookingRepository bookingRepository, BookingSlotRepository bookingSlotRepository) {
        this.bookingRepository = bookingRepository;
        this.bookingSlotRepository = bookingSlotRepository;
    }

    public Booking book(Long bookingSlotId, User reservee) {
        BookingSlot bookingSlot = bookingSlotRepository.findById(bookingSlotId).orElseThrow(() -> new RuntimeException("Slot " + bookingSlotId + " not found."));

        if (!bookingSlot.isActivated()) {
            throw new RuntimeException("Slot " + bookingSlotId + " is not active.");
        }

        //TO DO: Will need to check if its already booked unless it is a group meeting.

        return bookingRepository.save(new Booking(bookingSlot, reservee));
    }

    public void unbook(Long bookingId) {
        Booking booking = bookingRepository.findById(bookingId).orElseThrow(() -> new RuntimeException("Booking " + bookingId + " not found."));
        bookingRepository.delete(booking);
    }
}
