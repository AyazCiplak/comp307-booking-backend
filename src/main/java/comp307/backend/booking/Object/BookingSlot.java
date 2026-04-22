package comp307.backend.booking.Object;

import comp307.backend.account.Object.User;
import comp307.backend.booking.Object.Booking;

//TODO add some sort of indexing
public class BookingSlot {
    private TimeInterval timeInterval;
    private boolean activated = false;
    public BookingSlot(int beginHour, int beginMinute, int endHour, int endMinute) {
        this.timeInterval = new TimeInterval(beginHour, beginMinute, endHour, endMinute);
    }
    public void activate() {
        activated = true;
    }
    public void deactivate() {
        activated = false;
    }
    public boolean isActivated() {
        return activated;
    }
    private Booking booking;
    private void book(User reservee) {
        if (booking == null) {
            booking = new Booking(reservee);
        }

        // TODO add to DB
    }
    public Booking getBooking() {
        return booking;
    }
    // TODO Owner can view all slots and who booked a slot.
    private class TimeInterval {
        private Time beginTime;
        private Time endTime;
        public TimeInterval(int beginHour, int beginMinute, int endHour, int endMinute) {
            this.beginTime = new Time(beginHour, beginMinute);
            this.endTime = new Time(endHour, endMinute);
        }
        public String getInterval() {
            // TODO return time interval in format xx:xx - xx:xx
            return "";
        }
        private class Time {
            private int hour;
            private int minute;
            public Time(int hour, int minute) {
                this.hour = hour;
                this.minute = minute;
            }
            public int getHour() {
                return hour;
            }
            public int getMinute() {
                return minute;
            }
        }
    }

}
