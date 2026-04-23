package comp307.backend.booking;

import comp307.backend.account.Object.Owner;
import comp307.backend.account.Object.User;
import comp307.backend.booking.Object.BookingRepository;
import comp307.backend.booking.Object.BookingSlot;
import comp307.backend.booking.Object.TimeInterval;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

// TODO handle failures to process from calls
@RestController
@RequestMapping("api/booking")
public class BookingController {
    private final BookingService service;
    public BookingController(BookingRepository bookingRepository) {
        this.service = new BookingService(bookingRepository);
    }

    @PostMapping("/book")
    public ResponseEntity<BookingSlot> book(@RequestBody String userEmail, @RequestBody String ownerEmail, @RequestBody int beginHour, @RequestBody int beginMinute, @RequestBody int endHour, @RequestBody int endMinute) {
        return ResponseEntity.ok(service.book(userEmail, ownerEmail, beginHour, beginMinute, endHour, endMinute));
    }
    @PostMapping("/unbook")
    public ResponseEntity<Boolean> unbook(@RequestBody String userEmail, @RequestBody String ownerEmail, @RequestBody int beginHour, @RequestBody int beginMinute, @RequestBody int endHour, @RequestBody int endMinute) {
        service.unbook(userEmail, ownerEmail, beginHour, beginMinute, endHour, endMinute);
        return ResponseEntity.ok(true);
    }

    @PostMapping("/deleteSlot")
    public ResponseEntity<Boolean> deleteSlot(@RequestBody String ownerEmail, @RequestBody int beginHour, @RequestBody int beginMinute, @RequestBody int endHour, @RequestBody int endMinute) {
        service.delete(ownerEmail, beginHour, beginMinute, endHour, endMinute);
        return ResponseEntity.ok(true);
    }
    // TODO Users can email the owner of a slot
}
