//Programmed by Henry Niedermayer and Mao Yurun

package comp307.backend.booking.Service;

import comp307.backend.Exceptions.BadRequestException;
import org.springframework.stereotype.Service;

import comp307.backend.account.Object.User;
import comp307.backend.account.Object.UserRepository;
import comp307.backend.account.auth.AuthService;
import comp307.backend.booking.Entity.Booking;
import comp307.backend.booking.Entity.BookingSlot;
import comp307.backend.booking.Entity.GroupMeetingInstance;
import comp307.backend.booking.Repository.*;
import jakarta.transaction.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

//Service for Booking and BookingSlot (Type 2 and 3 only)
@Service
public class BookingService {
    private final AuthService authService;
    private final BookingRepository bookingRepository;
    private final BookingSlotRepository bookingSlotRepository;
    private final GroupMeetingInstanceRepository groupMeetingInstanceRepository;
    private final UserRepository userRepository;

    public BookingService(AuthService authService, BookingRepository bookingRepository, BookingSlotRepository bookingSlotRepository, GroupMeetingInstanceRepository groupMeetingInstanceRepository, UserRepository userRepository) {
        this.authService = authService;
        this.bookingRepository = bookingRepository;
        this.bookingSlotRepository = bookingSlotRepository;
        this.groupMeetingInstanceRepository = groupMeetingInstanceRepository;
        this.userRepository = userRepository;
    }



    //BOOKING SLOT
    //Type 3
    //startDateTimes/endDateTimes are not to be confused with listing all the weeks. Instead, they list the first weeks time slots. For example Monday Jan 1st 2/3 pm and Tuesday Jan 2nd 3/4 pm. 
    //From this we use weeksToRepeat to loop and make booking slots for the future weeks.
    public void createRecurringBookingSlot(String ownerToken, String title, List<LocalDateTime> startDateTimes, List<LocalDateTime> endDateTimes, int weeksToRepeat) {
        User owner = this.authService.authenticate(ownerToken);

        if (startDateTimes.size() != endDateTimes.size()) {
            throw new BadRequestException("Size of start date times and end date times do not match.");
        }

        if (!owner.isOwner()) {
            throw new BadRequestException("You are not an owner");
        }

        for (int i = 0; i < weeksToRepeat; i++) {
            for (int j = 0; j < startDateTimes.size(); j++) {
                LocalDateTime startDateTime = startDateTimes.get(j).plusWeeks(i);
                LocalDateTime endDateTime = endDateTimes.get(j).plusWeeks(i);

                bookingSlotRepository.save(new BookingSlot(owner, title, startDateTime, endDateTime));
            }
        }
    }

    //Type 2
    //Already enforced on front end that there can't be overlapping groups or end date earlier than start date etc.
    public BookingSlot createGroupMeetingBookingProposalSlot(Long groupMeetingInstanceID, String ownerToken, String title, LocalDateTime startDateTime, LocalDateTime endDateTime) {
        this.authService.authenticate(ownerToken);

        GroupMeetingInstance groupMeetingInstance = groupMeetingInstanceRepository.findById(groupMeetingInstanceID).orElseThrow(() -> new NoSuchElementException("Group meeting instance " + groupMeetingInstanceID + " not found."));

        if (!groupMeetingInstance.getOwner().getAccessToken().equals(ownerToken)) {
            throw new BadRequestException("You are a valid owner, but you do not own this group meeting instance.");
        }

        BookingSlot bookingSlot = new BookingSlot(groupMeetingInstance.getOwner(), title, startDateTime, endDateTime, groupMeetingInstance);
        return bookingSlotRepository.save(bookingSlot);
    }

    //Type 2
    //Selects one proposal slot, deletes the others and their bookings (availability markings)
    @Transactional
    public void selectGroupMeetingProposalSlot(Long bookingSlotId, String ownerToken) {
        BookingSlot selectedBookingSlot = bookingSlotRepository.findById(bookingSlotId).orElseThrow(() -> new NoSuchElementException("Slot " + bookingSlotId + " not found."));
        User owner = this.authService.authenticate(ownerToken);


        if (selectedBookingSlot.getSlotType() != BookingSlot.BookingSlotType.GROUP_PROPOSAL) {
            throw new BadRequestException("Slot " + bookingSlotId + " is not a group meeting proposal slot.");
        }

        if (selectedBookingSlot.getSlotStatus() == BookingSlot.BookingSlotStatus.CANCELLED) {
            throw new BadRequestException("Slot " + bookingSlotId + " was cancelled.");
        }

        if (!selectedBookingSlot.getOwner().equals(owner)) {
            throw new BadRequestException("You are not the owner of Slot " + bookingSlotId);
        }

        selectedBookingSlot.markAsSelected();
        bookingSlotRepository.save(selectedBookingSlot);

        List<BookingSlot> otherUnselectedProposals = bookingSlotRepository.findByGroupMeetingInstanceAndType(selectedBookingSlot.getGroupMeetingInstance(), BookingSlot.BookingSlotType.GROUP_PROPOSAL);

        for (BookingSlot otherProposal : otherUnselectedProposals) {
            List<Booking> unusedBookings = bookingRepository.findByBookingSlot(otherProposal);

            for (Booking booking : unusedBookings) {
                bookingRepository.delete(booking);
            }

            bookingSlotRepository.delete(otherProposal);
        }
    }

    public List<BookingSlot> getAllOwnedSlots(String ownerToken) {
        User owner = this.authService.authenticate(ownerToken);

        if (!owner.isOwner()) {
            throw new BadRequestException("You are not an owner");
        }

        return bookingSlotRepository.findByOwner(owner);

    }
    public List<BookingSlot> getAllAvailableOwnedSlots(String ownerEmail, String userToken) {
        this.authService.authenticate(userToken);
        User owner = userRepository.findById(ownerEmail).orElseThrow(() -> new NoSuchElementException("User " + ownerEmail + " not found."));

        if (!owner.isOwner()) {
            throw new BadRequestException(owner.getFirstName() + " " + owner.getLastName() + " is not an owner");
        }

        // Only expose OFFICE_HOURS and MEETING slots via the public browse API.
        // GROUP_PROPOSAL and GROUP_SELECTED slots are invite-only (accessible only via the invite URL).
        return bookingSlotRepository.findByOwner(owner).stream()
                .filter(s -> s.getSlotStatus() == BookingSlot.BookingSlotStatus.AVAILABLE)
                .filter(s -> s.getSlotType() == BookingSlot.BookingSlotType.OFFICE_HOURS
                          || s.getSlotType() == BookingSlot.BookingSlotType.MEETING)
                .toList();
    }


    public List<BookingSlot> getAllGroupMeetingProposalsForMeetingInstanceID(Long groupMeetingInstanceID, String token) {
        this.authService.authenticate(token);

        GroupMeetingInstance groupMeetingInstance = groupMeetingInstanceRepository.findById(groupMeetingInstanceID).orElseThrow(() -> new NoSuchElementException("Group meeting instance " + groupMeetingInstanceID + " not found."));

        return bookingSlotRepository.findByGroupMeetingInstanceAndType(groupMeetingInstance, BookingSlot.BookingSlotType.GROUP_PROPOSAL);
    }

    @Transactional
    public void cancelBookingSlot(String ownerToken, Long bookingSlotId) {
        BookingSlot bookingSlot = bookingSlotRepository.findById(bookingSlotId).orElseThrow(() -> new NoSuchElementException("Slot " + bookingSlotId + " not found."));
        User owner = this.authService.authenticate(ownerToken);

        if (!bookingSlot.getOwner().equals(owner)) {
            throw new BadRequestException("You are not the owner of slot " + bookingSlotId);
        }

        bookingSlot.setSlotStatus(BookingSlot.BookingSlotStatus.CANCELLED);
        bookingSlotRepository.save(bookingSlot);

        List<Booking> bookings = bookingRepository.findByBookingSlot(bookingSlot);
        for (Booking booking : bookings) {
            bookingRepository.delete(booking);
        }
    }

    //BOOKING
    //For type 3
    public Booking book(Long bookingSlotId, String reserveeToken) {
        User reservee = this.authService.authenticate(reserveeToken);
        BookingSlot bookingSlot = bookingSlotRepository.findById(bookingSlotId).orElseThrow(() -> new NoSuchElementException("Slot " + bookingSlotId + " not found."));

        if (!bookingSlot.getSlotStatus().equals(BookingSlot.BookingSlotStatus.AVAILABLE)) {
            throw new BadRequestException("Slot " + bookingSlotId + " is not available.");
        }

        if (bookingSlot.getSlotType() != BookingSlot.BookingSlotType.OFFICE_HOURS) {
            throw new BadRequestException("Slot " + bookingSlotId + " is not an office hours slot.");
        }

        if (bookingSlot.getOwner().equals(reservee)) {
            throw new BadRequestException("You should not be booking your own slot");
        }

        // Prevent duplicate bookings: the @UniqueConstraint on (bookingSlotID, reserveeEmail)
        // would otherwise throw a DataIntegrityViolationException 500
        boolean alreadyBooked = bookingRepository.findByBookingSlot(bookingSlot)
                .stream()
                .anyMatch(b -> b.getReservee().equals(reservee));
        if (alreadyBooked) {
            throw new BadRequestException("You have already booked slot " + bookingSlotId + ".");
        }

        //can never be full so still available.
        return bookingRepository.save(new Booking(bookingSlot, reservee));

    }
    
    //Type 2
    //Might need another function/modify this one to add the ability to join the Selected group meeting proposal that they haven't accepted yet.
    public Booking markAvailabilityForProposal(Long bookingSlotId, String reserveeToken) {
        User reservee = this.authService.authenticate(reserveeToken);
        BookingSlot bookingSlot = bookingSlotRepository.findById(bookingSlotId).orElseThrow(() -> new BadRequestException("Slot " + bookingSlotId + " not found."));

        if (bookingSlot.getSlotType() != BookingSlot.BookingSlotType.GROUP_PROPOSAL) {
            throw new BadRequestException("Slot " + bookingSlotId + " is not a pending proposal slot.");
        }

        if (bookingSlot.getSlotStatus() != BookingSlot.BookingSlotStatus.AVAILABLE) {
            throw new BadRequestException("Slot " + bookingSlotId + " is not available for marking availability.");
        }

        if (bookingSlot.getOwner().equals(reservee)) {
            throw new BadRequestException("You should not be booking your own slot");
        }

        int currentBookingsCount = bookingRepository.findByBookingSlot(bookingSlot).size();
        if (currentBookingsCount + 1 == bookingSlot.getMaxUsers()) {
            bookingSlot.setSlotStatus(BookingSlot.BookingSlotStatus.BOOKED);
            bookingSlotRepository.save(bookingSlot);
        }

        return bookingRepository.save(new Booking(bookingSlot, reservee));
    }

    /**
     * Returns a map of bookingSlotID -> booking count for every slot owned by the
     * authenticated owner. Used by the dashboard to populate the "X registered" badge
     * on each office-hour slot card.
     */
    public Map<Long, Long> getSlotBookingCounts(String ownerToken) {
        User owner = this.authService.authenticate(ownerToken);

        if (!owner.isOwner()) {
            throw new BadRequestException("You are not an owner");
        }

        List<BookingSlot> slots = bookingSlotRepository.findByOwner(owner);
        Map<Long, Long> counts = new HashMap<>();
        for (BookingSlot slot : slots) {
            counts.put(slot.getBookingSlotID(),
                    (long) bookingRepository.findByBookingSlot(slot).size());
        }
        return counts;
    }

    /** Returns all non-cancelled bookings for the authenticated user. */
    public List<Booking> getBookingsByReservee(String reserveeToken) {
        User reservee = this.authService.authenticate(reserveeToken);
        return bookingRepository.findByReservee(reservee).stream()
                .filter(b -> b.getBookingSlot().getSlotStatus() != BookingSlot.BookingSlotStatus.CANCELLED)
                .toList();
    }

    /**
     * Returns a map of bookingSlotID -> Booking for every MEETING-type slot owned by the
     * authenticated owner.  Used by the dashboard to display who booked each 1:1 meeting.
     * Slots with no booking yet (edge case) are omitted from the map.
     */
    public Map<Long, Booking> getSlotBookers(String ownerToken) {
        User owner = this.authService.authenticate(ownerToken);
        if (!owner.isOwner()) {
            throw new BadRequestException("You are not an owner");
        }

        List<BookingSlot> meetingSlots = bookingSlotRepository.findByOwner(owner).stream()
                .filter(s -> s.getSlotType() == BookingSlot.BookingSlotType.MEETING)
                .toList();

        Map<Long, Booking> result = new HashMap<>();
        for (BookingSlot slot : meetingSlots) {
            List<Booking> bookings = bookingRepository.findByBookingSlot(slot);
            if (!bookings.isEmpty()) {
                result.put(slot.getBookingSlotID(), bookings.get(0));
            }
        }
        return result;
    }

    /**
     * Returns a map of bookingSlotID -> booking count for every GROUP_PROPOSAL slot in the
     * given instance.  Any authenticated user can call this (no ownership check).
     * Used by GroupBooking.tsx to show the fill bar.
     */
    public Map<Long, Long> getGroupProposalCounts(Long groupMeetingInstanceID, String token) {
        this.authService.authenticate(token);
        GroupMeetingInstance instance = groupMeetingInstanceRepository.findById(groupMeetingInstanceID)
                .orElseThrow(() -> new NoSuchElementException("Group meeting instance " + groupMeetingInstanceID + " not found."));
        List<BookingSlot> slots = bookingSlotRepository.findByGroupMeetingInstanceAndType(
                instance, BookingSlot.BookingSlotType.GROUP_PROPOSAL);
        Map<Long, Long> result = new HashMap<>();
        for (BookingSlot slot : slots) {
            result.put(slot.getBookingSlotID(), (long) bookingRepository.findByBookingSlot(slot).size());
        }
        return result;
    }

    /**
     * Returns a map of bookingSlotID -> List<Booking> for every GROUP_PROPOSAL slot
     * in the given group meeting instance.  Used by ConfirmGroupTime.tsx to show who
     * marked themselves available on each proposed time slot.
     */
    public Map<Long, List<Booking>> getAllProposalBookers(Long groupMeetingInstanceID, String ownerToken) {
        User owner = this.authService.authenticate(ownerToken);
        GroupMeetingInstance instance = groupMeetingInstanceRepository.findById(groupMeetingInstanceID)
                .orElseThrow(() -> new NoSuchElementException("Group meeting instance " + groupMeetingInstanceID + " not found."));

        if (!instance.getOwner().equals(owner)) {
            throw new BadRequestException("You are not the owner of this group meeting instance.");
        }

        List<BookingSlot> proposalSlots = bookingSlotRepository.findByGroupMeetingInstanceAndType(
                instance, BookingSlot.BookingSlotType.GROUP_PROPOSAL);

        Map<Long, List<Booking>> result = new HashMap<>();
        for (BookingSlot slot : proposalSlots) {
            result.put(slot.getBookingSlotID(), bookingRepository.findByBookingSlot(slot));
        }
        return result;
    }

    //whether its type 2 or type 3, either way the booking will become available when unbooked because it either had infinite space or now has at least 1 space
    public void unbook(Long bookingId, String reserveeToken) {
        User reservee = this.authService.authenticate(reserveeToken);
        Booking booking = bookingRepository.findById(bookingId).orElseThrow(() -> new NoSuchElementException("Booking " + bookingId + " not found."));
        BookingSlot bookingSlot = booking.getBookingSlot();

        // No self check as it would't exist in the first place
        if (bookingSlot.getSlotStatus() == BookingSlot.BookingSlotStatus.CANCELLED) {
            throw new BadRequestException("Slot " + bookingSlot.getBookingSlotID() + " is cancelled, should not be calling this function.");
        }

        if (!booking.getReservee().equals(reservee)) {
            throw new BadRequestException("Illegal Access to booking " + bookingId);
        }

        bookingSlot.setSlotStatus(BookingSlot.BookingSlotStatus.AVAILABLE);
        bookingSlotRepository.save(bookingSlot);
        bookingRepository.delete(booking);
    }
}
