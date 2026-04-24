package comp307.backend.account.Object;

import java.time.LocalDateTime;
import java.util.ArrayList;

import comp307.backend.booking.Entity.Booking;
import comp307.backend.booking.Entity.BookingSlot;
import comp307.backend.booking.Repository.BookingRepository;
import comp307.backend.booking.Repository.BookingSlotRepository;

public class Owner extends User{
    //TODO should be retrieved in booking repository
    public Owner(String email, String password) {
        super(email, password);
    }

    @Override
    public boolean isOwner() {
        return true;
    }

    public BookingSlot createBookingSlot(BookingSlotRepository bookingSlotRepository, LocalDateTime startTime, LocalDateTime endTime) {
        BookingSlot bookingSlot = new BookingSlot(this, startTime, endTime);
        bookingSlotRepository.save(bookingSlot);
        return bookingSlot;
    }
    public boolean hasAvailableSlots(BookingRepository bookingRepository, BookingSlotRepository bookingSlotRepository) {
        for (BookingSlot bookingSlot : bookingSlotRepository.findByOwner(this)) {
            if (bookingSlot.isActivated()) {
                for (Booking booking : bookingRepository.findBySlotReserved(bookingSlot)) {
                    if (booking.getReservee() == null) {
                        return true;
                    }
                }
            }
        }

        return false;
    }

    public ArrayList<BookingSlot> getBookingSlots(User caller, BookingRepository bookingRepository, BookingSlotRepository bookingSlotRepository) {
        ArrayList<BookingSlot> bookingSlots = new ArrayList<>();

        for (BookingSlot bookingSlot : bookingSlotRepository.findByOwner(this)) {
            if (bookingSlot.isActivated()) {
                for (Booking booking : bookingRepository.findBySlotReserved(bookingSlot)) {
                    if (!bookingSlots.contains(bookingSlot) && booking.getReservee() == null) {
                        bookingSlots.add(bookingSlot);
                    }
                }
            }
        }

        return bookingSlots;
    }



}
