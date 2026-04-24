package comp307.backend.account.Object;

import java.util.ArrayList;

import comp307.backend.booking.Entity.BookingSlot;

public class Owner extends User{
    //TODO should be retrieved in booking repository
    private ArrayList<BookingSlot> bookingSlots = new ArrayList<>();
    public Owner(String email, String password) {
        super(email, password);
    }

    @Override
    public boolean isOwner() {
        return true;
    }

    public BookingSlot createBookingSlot(int beginHour, int beginMinute, int endHour, int endMinute) {
        BookingSlot bookingSlot = new BookingSlot(this, beginHour, beginMinute, endHour, endMinute);
        bookingSlots.add(bookingSlot);
        return bookingSlot;
    }
    public boolean hasAvailableSlots() {
        for (BookingSlot bookingSlot : bookingSlots) {
            if (bookingSlot.isActivated() && bookingSlot.getReservee() == null) {
                return true;
            }
        }

        return false;
    }

    public ArrayList<BookingSlot> getBookingSlots(User caller) {
        ArrayList<BookingSlot> bookingSlots = new ArrayList<>();

        for (BookingSlot bookingSlot : this.bookingSlots) {
            if (caller == this || bookingSlot.isAvailable())
                bookingSlots.add(bookingSlot);
        }

        return bookingSlots;
    }



}
