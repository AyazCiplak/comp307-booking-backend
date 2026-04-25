//Programmed by Henry Niedermayer and Mao Yurun

package comp307.backend.booking.ControllerAndDTO;

import comp307.backend.booking.Entity.Booking;
import comp307.backend.booking.Entity.BookingSlot;
import comp307.backend.booking.Service.BookingService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

// TODO handle failures to process from calls
@RestController
@RequestMapping("api/booking")
public class BookingController {
    private final BookingService bookingService;

    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @PostMapping("/book")
    public ResponseEntity<Booking> book(@RequestBody CreateBookingRequest request) {
        return ResponseEntity.ok(bookingService.book(request.getSlotId(), request.getReservee()));
    }

    @DeleteMapping("/{bookingSlotId}")
    public ResponseEntity<Void> unbook(@PathVariable Long bookingSlotId) {
        bookingService.unbook(bookingSlotId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/requests")
    public ResponseEntity<Void> requestBooking(@RequestBody RequestBookingRequest request) {
        bookingService.requestBooking(request.getRequesterEmail(), request.getOwnerEmail(), request.getRequestDate(), request.getStartTime(), request.getEndTime(), request.getMessage());
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/requests/{id}/accept")
    public ResponseEntity<Void> acceptRequest(@PathVariable(name = "id") Long requestID) {
        bookingService.acceptRequest(requestID);
        return ResponseEntity.noContent().build();
    }

    // TODO handleCancel

    // TODO handleDelete

    // TODO handleAcceptRequest

    // TODO handleDeclineRequest
}
