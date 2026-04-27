//Programmed by Henry Niedermayer and Mao Yurun

package comp307.backend.booking.Controllers;

import comp307.backend.account.Object.DataTransferObject.EmailTokenRequest;
import comp307.backend.booking.DTOs.CreateBookingRequest;
import comp307.backend.booking.DTOs.CreateGroupMeetingProposalSlot;
import comp307.backend.booking.DTOs.CreateRecurringBookingSlot;
import comp307.backend.booking.Entity.Booking;
import comp307.backend.booking.Entity.BookingSlot;
import comp307.backend.booking.Service.BookingService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;


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
        bookingService.createRecurringBookingSlot(request.getOwnerToken(), request.getTitle(), request.getStartDateTimes(), request.getEndDateTimes(), request.getWeeksToRepeat());
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/createGroupMeetingBookingProposalSlot")
    public ResponseEntity<BookingSlot> createGroupMeetingBookingProposalSlot(@RequestBody CreateGroupMeetingProposalSlot request) {
        return ResponseEntity.ok(bookingService.createGroupMeetingBookingProposalSlot(request.getGroupMeetingInstanceID(), request.getOwnerToken(), request.getTitle(), request.getStartDateTime(), request.getEndDateTime()));
    }

    @PatchMapping("/cancel/{bookingSlotId}")
    public ResponseEntity<Void> cancelBooking(@PathVariable Long bookingSlotId, @RequestBody String ownerToken) {
        bookingService.cancelBookingSlot(ownerToken, bookingSlotId);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/selectGroupMeetingProposalSlot/{bookingSlotId}")
    public ResponseEntity<Void> selectGroupMeetingProposalSlot(@PathVariable Long bookingSlotId, @RequestBody String ownerToken) {
        bookingService.selectGroupMeetingProposalSlot(bookingSlotId, ownerToken);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/getAllGroupMeetingProposalsForMeetingInstanceID/{groupMeetingInstanceID}")
    public ResponseEntity<List<BookingSlot>> getAllGroupMeetingProposalsForMeetingInstanceID(@PathVariable Long groupMeetingInstanceID, @RequestBody String token) {
        return ResponseEntity.ok(bookingService.getAllGroupMeetingProposalsForMeetingInstanceID(groupMeetingInstanceID, token));
    }

    @GetMapping("/owner/getAllAvailableOwnedSlots")
    public ResponseEntity<List<BookingSlot>> getAllAvailableOwnedSlots(@RequestParam EmailTokenRequest request) {
        return ResponseEntity.ok(bookingService.getAllAvailableOwnedSlots(request.getEmail(), request.getToken()));
    }
    
    @GetMapping("/owner/getAllOwnedSlots")
    public ResponseEntity<List<BookingSlot>> getAllOwnedSlots(@RequestParam String targetEmail) {
        return ResponseEntity.ok(bookingService.getAllOwnedSlots(targetEmail));
    }

    @PostMapping("/book")
    public ResponseEntity<Booking> book(@RequestBody CreateBookingRequest request) {
        return ResponseEntity.ok(bookingService.book(request.getSlotId(), request.getReserveeToken()));
    }

    @PostMapping("/markAvailabilityForProposal")
    public ResponseEntity<Booking> markAvailabilityForProposal(@RequestBody CreateBookingRequest request) {
        return ResponseEntity.ok(bookingService.markAvailabilityForProposal(request.getSlotId(), request.getReserveeToken()));
    }

    @DeleteMapping("/{bookingId}")
    public ResponseEntity<Void> unbook(@PathVariable Long bookingId, @RequestBody String reserveeToken) {
        bookingService.unbook(bookingId, reserveeToken);
        return ResponseEntity.noContent().build();
    }
    
    // TODO handleDelete (or just use the cancel endpoint?)

    @ExceptionHandler(value = NoSuchElementException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<String> handleNoSuchElementException(NoSuchElementException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
    }
    @ExceptionHandler(value = IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ResponseEntity<String> handleIllegalArgumentException(IllegalArgumentException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.FORBIDDEN);
    }
}
