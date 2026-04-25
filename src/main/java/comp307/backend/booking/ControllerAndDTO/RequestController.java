package comp307.backend.booking.ControllerAndDTO;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import comp307.backend.booking.Service.RequestService;

@RestController
@RequestMapping("api/requests")
public class RequestController {
    private final RequestService requestService;

    public RequestController(RequestService requestService) {
        this.requestService = requestService;
    }

    @PostMapping("/requests")
    public ResponseEntity<Void> requestBooking(@RequestBody RequestBookingRequest request) {
        requestService.requestBooking(request.getRequesterEmail(), request.getOwnerEmail(), request.getRequestDate(), request.getStartTime(), request.getEndTime(), request.getMessage());
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/requests/{id}/accept")
    public ResponseEntity<Void> acceptRequest(@PathVariable(name = "id") Long requestID) {
        requestService.acceptRequest(requestID);
        return ResponseEntity.noContent().build();
    }

    // TODO handleAcceptRequest

    // TODO handleDeclineRequest
}
