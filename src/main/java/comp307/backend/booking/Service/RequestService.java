package comp307.backend.booking.Service;

import java.sql.Time;

import org.springframework.stereotype.Service;

import comp307.backend.booking.Repository.RequestRepository;

@Service
public class RequestService {
    private final RequestRepository requestRepository;
    
    public RequestService(RequestRepository requestRepository) {
        this.requestRepository = requestRepository;
    }


    public void requestBooking(String requesterEmail, String ownerEmail, Date requestedDate, Time requestedStart, Time requestedEnd, String message) {
        requestRepository.save(new Request(requesterEmail, ownerEmail, requestedDate, requestedStart, requestedEnd, message));
    }

    public void acceptRequest(Long requestID) {
        Optional<Request> request = requestRepository.findById(requestID);

        if (request.isPresent()) {

        }
    }
}
