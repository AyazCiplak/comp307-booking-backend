//Programmed by Henry Niedermayer

package comp307.backend.booking.ControllerAndDTO;

import java.time.LocalDateTime;
import java.util.List;

public class CreateRecurringBookingSlot {
    private String ownerEmail;
    private List<LocalDateTime> startDateTimes;
    private List<LocalDateTime> endDateTimes;
    private int weeksToRepeat;

    public String getOwnerEmail() {
        return this.ownerEmail;
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
