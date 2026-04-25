package comp307.backend.booking.Service;

import java.sql.Date;
import java.sql.Time;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import comp307.backend.booking.Entity.Request;
import org.springframework.stereotype.Service;

import comp307.backend.booking.Repository.RequestRepository;

@Service
public class RequestService {
    private final RequestRepository requestRepository;
    
    public RequestService(RequestRepository requestRepository) {
        this.requestRepository = requestRepository;
    }


    public void requestBooking(String requesterEmail, String ownerEmail, LocalDateTime requestedStart, LocalDateTime requestedEnd, String message) {
        requestRepository.save(new Request(requesterEmail, ownerEmail, requestedStart, requestedEnd, message));
    }

    public void acceptRequest(Long requestID) {
        Optional<Request> request = requestRepository.findById(requestID);

        if (request.isPresent()) {
            request.get().setStatus(true);
        }
    }

    public void declineRequest(Long requestID) {
        Optional<Request> request = requestRepository.findById(requestID);

        if (request.isPresent()) {
            request.get().setStatus(false);
            requestRepository.delete(request.get());
        }

    }

    public List<Request> getPendingRequests() {
        return requestRepository.findByStatus(Request.RequestStatus.PENDING);
    }
}
