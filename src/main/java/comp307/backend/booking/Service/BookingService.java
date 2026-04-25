package comp307.backend.booking.Service;

import comp307.backend.booking.Entity.Request;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import comp307.backend.account.Object.User;
import comp307.backend.booking.Entity.Booking;
import comp307.backend.booking.Entity.BookingSlot;
import comp307.backend.booking.Repository.*;

import java.sql.Time;
import java.sql.Timestamp;
import java.util.Date;
import java.util.Optional;

@Service
public class BookingService {
    private final BookingRepository bookingRepository;
    private final BookingSlotRepository bookingSlotRepository;
    private final RequestRepository requestRepository;
    private final JavaMailSender mailSender;

    public BookingService(BookingRepository bookingRepository, BookingSlotRepository bookingSlotRepository, RequestRepository requestRepository, JavaMailSender mailSender) {
        this.bookingRepository = bookingRepository;
        this.bookingSlotRepository = bookingSlotRepository;
        this.requestRepository = requestRepository;
        this.mailSender = mailSender;
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
    public void requestBooking(String requesterEmail, String ownerEmail, Date requestedDate, Time requestedStart, Time requestedEnd, String message) {
        requestRepository.save(new Request(requesterEmail, ownerEmail, requestedDate, requestedStart, requestedEnd, message));
    }

    public void acceptRequest(Long requestID) {
        Optional<Request> request = requestRepository.findById(requestID);

        if (request.isPresent()) {

        }
    }
}
