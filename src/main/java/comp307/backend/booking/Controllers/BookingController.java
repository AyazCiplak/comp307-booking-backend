//Programmed by Henry Niedermayer, Mao Yurun and Ayaz Ciplak

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
import java.util.Map;
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

    @PostMapping("/getAllGroupMeetingProposalsForMeetingInstanceID/{groupMeetingInstanceID}")
    public ResponseEntity<List<BookingSlot>> getAllGroupMeetingProposalsForMeetingInstanceID(@PathVariable Long groupMeetingInstanceID, @RequestBody String token) {
        return ResponseEntity.ok(bookingService.getAllGroupMeetingProposalsForMeetingInstanceID(groupMeetingInstanceID, token));
    }

    @PostMapping("/owner/getAllAvailableOwnedSlots")
    public ResponseEntity<List<BookingSlot>> getAllAvailableOwnedSlots(@RequestBody EmailTokenRequest request) {
        return ResponseEntity.ok(bookingService.getAllAvailableOwnedSlots(request.getEmail(), request.getToken()));
    }
    
    @PostMapping("/owner/getAllOwnedSlots")
    public ResponseEntity<List<BookingSlot>> getAllOwnedSlots(@RequestBody String ownerToken) {
        return ResponseEntity.ok(bookingService.getAllOwnedSlots(ownerToken));
    }

    /**
     * POST /api/booking/owner/getSlotBookingCounts
     * Returns a map of bookingSlotID (as string key) -> booking count for every slot
     * owned by the authenticated owner. Used by the dashboard to show "X registered".
     */
    @PostMapping("/owner/getSlotBookingCounts")
    public ResponseEntity<Map<Long, Long>> getSlotBookingCounts(@RequestBody String ownerToken) {
        return ResponseEntity.ok(bookingService.getSlotBookingCounts(ownerToken));
    }

    /**
     * POST /api/booking/owner/getSlotBookers
     * Returns a map of bookingSlotID -> Booking for every MEETING-type slot owned by the
     * authenticated owner.  Used by the dashboard to show the booker's name/email on 1:1 slots.
     * Body = raw owner token.
     */
    @PostMapping("/owner/getSlotBookers")
    public ResponseEntity<Map<Long, Booking>> getSlotBookers(@RequestBody String ownerToken) {
        return ResponseEntity.ok(bookingService.getSlotBookers(ownerToken));
    }

    /**
     * POST /api/booking/getGroupProposalCounts/{groupMeetingInstanceID}
     * Returns a map of bookingSlotID -> availability count for every GROUP_PROPOSAL slot.
     * Any authenticated user can call this (no ownership check). Body = raw token.
     */
    @PostMapping("/getGroupProposalCounts/{groupMeetingInstanceID}")
    public ResponseEntity<Map<Long, Long>> getGroupProposalCounts(
            @PathVariable Long groupMeetingInstanceID,
            @RequestBody String token) {
        return ResponseEntity.ok(bookingService.getGroupProposalCounts(groupMeetingInstanceID, token));
    }

    /**
     * POST /api/booking/getAllProposalBookers/{groupMeetingInstanceID}
     * Returns a map of bookingSlotID -> List<Booking> for every GROUP_PROPOSAL slot
     * in the given group meeting instance. Body = raw owner token.
     */
    @PostMapping("/getAllProposalBookers/{groupMeetingInstanceID}")
    public ResponseEntity<Map<Long, List<Booking>>> getAllProposalBookers(
            @PathVariable Long groupMeetingInstanceID,
            @RequestBody String ownerToken) {
        return ResponseEntity.ok(bookingService.getAllProposalBookers(groupMeetingInstanceID, ownerToken));
    }

    @PostMapping("/book")
    public ResponseEntity<Booking> book(@RequestBody CreateBookingRequest request) {
        return ResponseEntity.ok(bookingService.book(request.getBookingSlotID(), request.getReserveeToken()));
    }

    @PostMapping("/markAvailabilityForProposal")
    public ResponseEntity<Booking> markAvailabilityForProposal(@RequestBody CreateBookingRequest request) {
        return ResponseEntity.ok(bookingService.markAvailabilityForProposal(request.getBookingSlotID(), request.getReserveeToken()));
    }

    /**
     * POST /api/booking/getMyBookings
     * Returns all non-cancelled bookings (Booking objects with nested BookingSlot) for the
     * authenticated user. The bookingID is needed by the frontend to call the unbook endpoint.
     */
    @PostMapping("/getMyBookings")
    public ResponseEntity<List<Booking>> getMyBookings(@RequestBody String reserveeToken) {
        return ResponseEntity.ok(bookingService.getBookingsByReservee(reserveeToken));
    }

    @DeleteMapping("/{bookingId}")
    public ResponseEntity<Void> unbook(@PathVariable Long bookingId, @RequestBody String reserveeToken) {
        bookingService.unbook(bookingId, reserveeToken);
        return ResponseEntity.noContent().build();
    }
    


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
