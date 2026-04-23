package comp307.backend.account.Object;

import comp307.backend.booking.Object.BookingSlot;

import java.util.ArrayList;

public class Owner extends User{
    //TODO use better data structure
    private ArrayList<BookingSlot> bookingSlots = new ArrayList<>();
    public Owner(String email, String password) {
        super(email, password);
    }

    @Override
    public boolean isOwner() {
        return true;
    }

    public void createBookingSlot(int beginHour, int beginMinute, int endHour, int endMinute) {
        bookingSlots.add(new BookingSlot(beginHour, beginMinute, endHour, endMinute));
    }
    public boolean hasAvailableSlots() {
        for (BookingSlot bookingSlot : bookingSlots) {
            if (bookingSlot.isActivated() && bookingSlot.getBooking() == null) {
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
