//Programmed by Henry Niedermayer and Mao Yurun
package comp307.backend.booking.Controllers;

import comp307.backend.booking.DTOs.RequestBookingRequest;
import comp307.backend.booking.Entity.Request;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import comp307.backend.booking.Service.RequestService;

import javax.security.auth.login.AccountNotFoundException;
import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("api/requests")
public class RequestController {
    private final RequestService requestService;

    public RequestController(RequestService requestService) {
        this.requestService = requestService;
    }

    @PostMapping("/requestBooking")
    public ResponseEntity<Request> requestBooking(@RequestBody RequestBookingRequest request) {
        return ResponseEntity.ok(requestService.requestBooking(request.getRequesterToken(), request.getOwnerEmail(), request.getStartTime(), request.getEndTime(), request.getMessage()));
    }

    @PostMapping("/{id}/accept")
    public ResponseEntity<Void> acceptRequest(@PathVariable(name = "id") Long requestID, @RequestBody String ownerToken) {
        requestService.setRequestState(requestID, ownerToken, true);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/decline")
    public ResponseEntity<Void> declineRequest(@PathVariable(name = "id") Long requestID, @RequestBody String ownerToken) {
        requestService.setRequestState(requestID, ownerToken, false);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/getAllPendingRequests")
    public ResponseEntity<List<Request>> getAllPendingRequests(@PathVariable String ownerToken) {
        return ResponseEntity.ok(requestService.getPendingRequests(ownerToken));
    }

    @ExceptionHandler(value = NoSuchElementException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<String> handleNoSuchElementException(NoSuchElementException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
    }
}
