//Programmed by Henry Niedermayer

package comp307.backend.booking.DTOs;

import java.time.LocalDateTime;
import java.util.List;

public class CreateRecurringBookingSlot {
    private String ownerEmail;
    private String title;
    private List<LocalDateTime> startDateTimes;
    private List<LocalDateTime> endDateTimes;
    private int weeksToRepeat;

    public String getOwnerEmail() {
        return this.ownerEmail;
    }

    public String getTitle() {
        return this.title;
    }

    public List<LocalDateTime> getStartDateTimes() {
        return this.startDateTimes;
    }

    public List<LocalDateTime> getEndDateTimes() {
        return this.endDateTimes;
    }

    public int getWeeksToRepeat() {
        return this.weeksToRepeat;
    }
}
