//Programmed by Henry Niedermayer and Mao Yurun

package comp307.backend.booking.ControllerAndDTO;

import comp307.backend.account.Object.DataTransferObject.EmailTokenRequest;
import comp307.backend.booking.Entity.Booking;
import comp307.backend.booking.Entity.BookingSlot;
import comp307.backend.booking.Service.BookingService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


// TODO handle failures to process from calls
//For booking and booking slots (Type 2 and 3 only)
@RestController
@RequestMapping("api/booking")
public class BookingController {
    private final BookingService bookingService;

    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @PostMapping("/createRecurringBookingSlot")
    public ResponseEntity<Void> createRecurringBookingSlot(@RequestBody CreateRecurringBookingSlot request) {
        bookingService.createRecurringBookingSlot(request.getOwnerToken(), request.getStartDateTimes(), request.getEndDateTimes(), request.getWeeksToRepeat());
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/cancel/{bookingSlotId}")
    public ResponseEntity<Void> cancelBooking(@PathVariable Long bookingSlotId, @RequestBody String ownerToken) {
        bookingService.cancelBookingSlot(ownerToken, bookingSlotId);
        return ResponseEntity.noContent().build();
    }

    // An user getting all available slots from an owner
    @PostMapping("/owner/getAllAvailableOwnedSlots")
    public ResponseEntity<List<BookingSlot>> getAllAvailableOwnedSlots(@RequestBody EmailTokenRequest request) {
        return ResponseEntity.ok(bookingService.getAllAvailableOwnedSlots(request.getEmail(), request.getToken()));
    }

    // An Owner getting all slots they opened
    @PostMapping("/owner/getAllOwnedSlots")
    public ResponseEntity<List<BookingSlot>> getAllOwnedSlots(@RequestBody String ownerToken) {
        return ResponseEntity.ok(bookingService.getAllOwnedSlots(ownerToken));
    }

    @PostMapping("/book")
    public ResponseEntity<Booking> book(@RequestBody CreateBookingRequest request) {
        return ResponseEntity.ok(bookingService.book(request.getSlotId(), request.getReserveeToken()));
    }

    @DeleteMapping("/{bookingSlotId}")
    public ResponseEntity<Void> unbook(@PathVariable Long bookingSlotId, @RequestBody String reserveeToken) {
        bookingService.unbook(bookingSlotId, reserveeToken);
        return ResponseEntity.noContent().build();
    }
    
    // TODO handleDelete (or just use the cancel endpoint?)
}
