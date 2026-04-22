package comp307.backend.account.Object;

import comp307.backend.booking.Object.BookingSlot;

import java.util.ArrayList;

public class Owner extends User{
    //TODO use better data structure
    private ArrayList<BookingSlot> bookingSlots = new ArrayList<>();
    public Owner(String firstName, String lastName, String email, String password) {
        super(firstName, lastName, email, password);
    }

    @Override
    public boolean isOwner() {
        return true;
    }

    public void createBookingSlot(int beginHour, int beginMinute, int endHour, int endMinute) {
        bookingSlots.add(new BookingSlot(beginHour, beginMinute, endHour, endMinute));
    }

    public ArrayList<BookingSlot> getBookingSlots(User caller) {
        //TODO improve logic,
        ArrayList<BookingSlot> bookingSlots = new ArrayList<>();
        for (BookingSlot bookingSlot : this.bookingSlots) {
            if (caller == this || bookingSlot.isActivated())
                bookingSlots.add(bookingSlot);
        }

        return bookingSlots;
    }

    public String generateURL() {
        // TODO
        return "";
    }

    // TODO Owner can delete the booking slot

    // TODO Owner can send an email to the booked person
}
