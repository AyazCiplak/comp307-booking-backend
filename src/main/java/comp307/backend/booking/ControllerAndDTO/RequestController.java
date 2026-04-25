package comp307.backend.booking.ControllerAndDTO;

import comp307.backend.booking.Entity.Request;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import comp307.backend.booking.Service.RequestService;

import java.util.List;

@RestController
@RequestMapping("api/requests")
public class RequestController {
    private final RequestService requestService;

    public RequestController(RequestService requestService) {
        this.requestService = requestService;
    }

    @PostMapping("/requestBooking")
    public ResponseEntity<Request> requestBooking(@RequestBody RequestBookingRequest request) {
        return ResponseEntity.ok(requestService.requestBooking(request.getRequesterEmail(), request.getOwnerEmail(), request.getStartTime(), request.getEndTime(), request.getMessage()));
    }

    @PostMapping("/{id}/accept")
    public ResponseEntity<Void> acceptRequest(@PathVariable(name = "id") Long requestID) {
        requestService.acceptRequest(requestID);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/decline")
    public ResponseEntity<Void> declineRequest(@PathVariable(name = "id") Long requestID) {
        requestService.declineRequest(requestID);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{ownerEmail}/getAllPendingRequests")
    public ResponseEntity<List<Request>> getAllPendingRequests(@PathVariable String ownerEmail) {
        return ResponseEntity.ok(requestService.getPendingRequests(ownerEmail));
    }
}
