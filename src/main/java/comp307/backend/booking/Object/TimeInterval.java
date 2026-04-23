package comp307.backend.booking.Object;

public class TimeInterval {
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

