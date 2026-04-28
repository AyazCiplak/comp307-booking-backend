//Programmed by Mao Yurun and Ayaz Ciplak
package comp307.backend.booking.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import comp307.backend.Exceptions.BadRequestException;
import comp307.backend.account.Object.User;
import comp307.backend.account.Object.UserRepository;
import comp307.backend.account.auth.AuthService;
import comp307.backend.booking.Entity.Booking;
import comp307.backend.booking.Entity.BookingSlot;
import comp307.backend.booking.Entity.Request;
import comp307.backend.booking.Repository.BookingRepository;
import comp307.backend.booking.Repository.BookingSlotRepository;
import comp307.backend.booking.Repository.RequestRepository;
import org.springframework.stereotype.Service;

@Service
public class RequestService {
    private final AuthService authService;
    private final UserRepository userRepository;
    private final RequestRepository requestRepository;
    private final BookingSlotRepository bookingSlotRepository;
    private final BookingRepository bookingRepository;

    // Type 1 meeting
    public RequestService(AuthService authService,
                          UserRepository userRepository,
                          RequestRepository requestRepository,
                          BookingSlotRepository bookingSlotRepository,
                          BookingRepository bookingRepository) {
        this.authService = authService;
        this.userRepository = userRepository;
        this.requestRepository = requestRepository;
        this.bookingSlotRepository = bookingSlotRepository;
        this.bookingRepository = bookingRepository;
    }

    public Request requestBooking(String requesterToken, String ownerEmail,
                                  LocalDateTime requestedStart, LocalDateTime requestedEnd,
                                  String message) {
        User requester = this.authService.authenticate(requesterToken);
        User owner = userRepository.findById(ownerEmail)
                .orElseThrow(() -> new NoSuchElementException("User " + ownerEmail + " not found"));

        if (owner.equals(requester)) {
            throw new BadRequestException("You should not send a request to yourself");
        }

        if (!owner.isOwner()) {
            throw new BadRequestException(owner.getFirstName() + " " + owner.getLastName() + " is not an owner");
        }

        return requestRepository.save(
                new Request(requester, owner, requestedStart, requestedEnd, message));
    }

    /**
     * Owner accepts or declines a pending request.
     *
     * When accepting: marks the Request as ACCEPTED, then creates a BookingSlot
     * (OFFICE_HOURS type, status immediately BOOKED) + a Booking for the requester.
     * This makes the meeting appear in the requester's "My Appointments" via getMyBookings.
     *
     * When declining: the Request row is deleted.
     */
    public void setRequestState(Long requestID, String ownerToken, boolean accept) {
        Request request = requestRepository.findById(requestID)
                .orElseThrow(() -> new NoSuchElementException("Request No." + requestID + " not found"));
        User owner = this.authService.authenticate(ownerToken);

        if (!request.isPending()) {
            throw new BadRequestException("Request No." + requestID + " is not pending");
        }

        if (!request.getOwner().equals(owner)) {
            throw new BadRequestException("You are not the owner of Request No." + requestID);
        }

        if (accept) {
            // Mark the request as accepted so requester's getMyRequests won't return it anymore
            request.setStatus(true);
            requestRepository.save(request);

            // Create a BookingSlot for the owner representing this 1:1 meeting.
            // Title = "Meeting with [requester name]" so the owner sees who it's with.
            String title = "Meeting with "
                    + request.getRequester().getFirstName()
                    + " " + request.getRequester().getLastName();
            BookingSlot slot = new BookingSlot(owner, title,
                    request.getRequestedStart(), request.getRequestedEnd(),
                    BookingSlot.BookingSlotType.MEETING);
            // Mark as BOOKED immediately — this is a private 1:1 slot, not open to others
            slot.setSlotStatus(BookingSlot.BookingSlotStatus.BOOKED);
            BookingSlot savedSlot = bookingSlotRepository.save(slot);

            // Create the Booking that links the requester to the new slot
            bookingRepository.save(new Booking(savedSlot, request.getRequester()));
        } else {
            requestRepository.delete(request);
        }
    }

    public List<Request> getPendingRequests(String ownerToken) {
        ArrayList<Request> requests = new ArrayList<>();
        User owner = this.authService.authenticate(ownerToken);

        if (!owner.isOwner()) {
            throw new BadRequestException("You are not an owner");
        }

        for (Request request : requestRepository.findByOwner(owner)) {
            if (request.isPending()) {
                requests.add(request);
            }
        }

        return requests;
    }

    /**
     * Returns all PENDING requests sent by the authenticated user.
     * Accepted requests are intentionally excluded here — once accepted, a Booking
     * is created for the requester, so it will appear via getMyBookings instead.
     */
    public List<Request> getMyRequests(String requesterToken) {
        User requester = this.authService.authenticate(requesterToken);
        return requestRepository.findByRequester(requester).stream()
                .filter(Request::isPending)
                .toList();
    }

    /**
     * Allows the original requester to cancel (delete) their own pending request.
     * Only PENDING requests can be cancelled this way.
     */
    public void cancelMyRequest(Long requestId, String requesterToken) {
        User requester = this.authService.authenticate(requesterToken);
        Request request = requestRepository.findById(requestId)
                .orElseThrow(() -> new NoSuchElementException("Request No." + requestId + " not found"));

        if (!request.getRequester().equals(requester)) {
            throw new BadRequestException("You did not send Request No." + requestId);
        }

        if (!request.isPending()) {
            throw new BadRequestException("Request No." + requestId + " is no longer pending");
        }

        requestRepository.delete(request);
    }
}
