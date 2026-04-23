package comp307.backend.booking.Controller;

import comp307.backend.booking.Entity.BookingSlot;
import comp307.backend.booking.Repository.BookingSlotRepository;
import comp307.backend.booking.Service.BookingServiceold;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

// TODO handle failures to process from calls
@RestController
@RequestMapping("api/booking")
public class BookingController {
    private final BookingServiceold service;
    public BookingController(BookingSlotRepository bookingRepository) {
        this.service = new BookingServiceold(bookingRepository);
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
}
