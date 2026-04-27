//Programmed by Henry Niedermayer and Mao Yurun

package comp307.backend.booking.Controllers;

import comp307.backend.booking.DTOs.CreateBookingRequest;
import comp307.backend.booking.DTOs.CreateGroupMeetingProposalSlot;
import comp307.backend.booking.DTOs.CreateRecurringBookingSlot;
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
        bookingService.createRecurringBookingSlot(request.getOwnerEmail(), request.getStartDateTimes(), request.getEndDateTimes(), request.getWeeksToRepeat());
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/createGroupMeetingBookingProposalSlot")
    public ResponseEntity<BookingSlot> createGroupMeetingBookingProposalSlot(@RequestBody CreateGroupMeetingProposalSlot request) {
        return ResponseEntity.ok(bookingService.createGroupMeetingBookingProposalSlot(request.getGroupMeetingInstanceID(), request.getTitle(), request.getStartDateTime(), request.getEndDateTime()));
    }

    @PatchMapping("/cancel/{bookingSlotId}")
    public ResponseEntity<Void> cancelBooking(@PathVariable Long bookingSlotId) {
        bookingService.cancelBookingSlot(bookingSlotId);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/selectGroupMeetingProposalSlot/{bookingSlotId}")
    public ResponseEntity<Void> selectGroupMeetingProposalSlot(@PathVariable Long bookingSlotId) {
        bookingService.selectGroupMeetingProposalSlot(bookingSlotId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/owner/getAllAvailableOwnedSlots")
    public ResponseEntity<List<BookingSlot>> getAllAvailableOwnedSlots(@RequestBody String targetEmail) {
        return ResponseEntity.ok(bookingService.getAllAvailableOwnedSlots(targetEmail));
    }
    
    @PostMapping("/owner/getAllOwnedSlots")
    public ResponseEntity<List<BookingSlot>> getAllOwnedSlots(@RequestBody String targetEmail) {
        return ResponseEntity.ok(bookingService.getAllOwnedSlots(targetEmail));
    }

    @PostMapping("/book")
    public ResponseEntity<Booking> book(@RequestBody CreateBookingRequest request) {
        return ResponseEntity.ok(bookingService.book(request.getSlotId(), request.getReserveeEmail()));
    }

    @PostMapping("/markAvailabilityForProposal")
    public ResponseEntity<Booking> markAvailabilityForProposal(@RequestBody CreateBookingRequest request) {
        return ResponseEntity.ok(bookingService.markAvailabilityForProposal(request.getSlotId(), request.getReserveeEmail()));
    }

    @DeleteMapping("/{bookingId}")
    public ResponseEntity<Void> unbook(@PathVariable Long bookingId) {
        bookingService.unbook(bookingId);
        return ResponseEntity.noContent().build();
    }
    
    // TODO handleDelete (or just use the cancel endpoint?)
}
