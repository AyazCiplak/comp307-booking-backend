//Programmed by Henry Niedermayer

package comp307.backend.booking.Service;

import java.time.LocalDateTime;

import comp307.backend.account.Object.User;
import org.springframework.stereotype.Service;

import comp307.backend.booking.Entity.BookingSlot;
import comp307.backend.booking.Repository.BookingSlotRepository;

@Service
public class BookingSlotService {
    private final BookingSlotRepository bookingSlotRepository;

    public BookingSlotService(BookingSlotRepository bookingSlotRepository) {
        this.bookingSlotRepository = bookingSlotRepository;
    }

    public BookingSlot createBookingSlot(User owner, LocalDateTime start, LocalDateTime end) {
        BookingSlot bookingSlot = new BookingSlot(owner, start, end);
        return bookingSlotRepository.save(bookingSlot);
    }
}
