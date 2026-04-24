//Programmed by Henry Niedermayer

package comp307.backend.booking.Service;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

import comp307.backend.account.Object.Owner;
import comp307.backend.booking.Entity.BookingSlot;
import comp307.backend.booking.Repository.BookingSlotRepository;

@Service
public class BookingSlotService {
    private final BookingSlotRepository bookingSlotRepository;

    public BookingSlotService(BookingSlotRepository bookingSlotRepository) {
        this.bookingSlotRepository = bookingSlotRepository;
    }

    public BookingSlot createBookingSlot(Owner owner, LocalDateTime start, LocalDateTime end) {
        BookingSlot bookingSlot = new BookingSlot(owner, start, end);
        return bookingSlotRepository.save(bookingSlot);
    }

    public BookingSlot activateBookingSlot(Long bookingSlotID) {
        BookingSlot bookingSlot = bookingSlotRepository.findById(bookingSlotID).orElseThrow(() -> new RuntimeException("Slot " + bookingSlotID + " not found."));

        bookingSlot.activate();
        return bookingSlotRepository.save(bookingSlot);
    }
}
