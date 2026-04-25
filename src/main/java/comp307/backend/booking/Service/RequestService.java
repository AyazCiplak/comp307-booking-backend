package comp307.backend.booking.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import comp307.backend.account.Object.User;
import comp307.backend.account.Object.UserRepository;
import comp307.backend.booking.Entity.Request;
import org.springframework.stereotype.Service;

import comp307.backend.booking.Repository.RequestRepository;

//TODO error handling
@Service
public class RequestService {
    private final UserRepository userRepository;
    private final RequestRepository requestRepository;

    public RequestService(UserRepository userRepository, RequestRepository requestRepository) {
        this.userRepository = userRepository;
        this.requestRepository = requestRepository;
    }


    public Request requestBooking(String requesterEmail, String ownerEmail, LocalDateTime requestedStart, LocalDateTime requestedEnd, String message) {
        User requester = userRepository.findById(requesterEmail).orElseThrow(() -> new RuntimeException("User " + requesterEmail + " not found."));
        User owner = userRepository.findById(ownerEmail).orElseThrow(() -> new RuntimeException("User " + ownerEmail + " not found."));

        Request request = new Request(requester, owner, requestedStart, requestedEnd, message);
        requestRepository.save(request);

        return request;
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

    public List<Request> getPendingRequests(String ownerEmail) {
        ArrayList<Request> requests = new ArrayList<>();
        Optional<User> owner = userRepository.findById(ownerEmail);
        if (owner.isEmpty()) return requests;

        for (Request request : requestRepository.findByOwner(owner.get())) {
            if (request.isPending()) {
                requests.add(request);
            }
        }
        return requests;
    }
}
