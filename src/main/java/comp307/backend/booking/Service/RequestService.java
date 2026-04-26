//Programmed by Mao Yurun
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

    // Type 1 meeting
    public RequestService(UserRepository userRepository, RequestRepository requestRepository) {
        this.userRepository = userRepository;
        this.requestRepository = requestRepository;
    }


    public Request requestBooking(String requesterToken, String ownerEmail, LocalDateTime requestedStart, LocalDateTime requestedEnd, String message) {
        User requester = userRepository.findById(requesterToken).orElseThrow(() -> new RuntimeException("User " + requesterToken + " not found."));
        User owner = userRepository.findById(ownerEmail).orElseThrow(() -> new RuntimeException("User " + ownerEmail + " not found."));

        if (!owner.isOwner()) return null;
        Request request = new Request(requester, owner, requestedStart, requestedEnd, message);
        requestRepository.save(request);

        return request;
    }

    public void setRequestState(Long requestID, String ownerToken, boolean accept) {
        Optional<Request> request = requestRepository.findById(requestID);
        User owner = userRepository.findByaccessToken(ownerToken).orElseThrow(() -> new RuntimeException("User " + ownerToken + " not found."));

        if (request.isPresent() && request.get().isPending() && request.get().getOwner().equals(owner)) {
            if (accept) {
                request.get().setStatus(true);
            } else {
                request.get().setStatus(false);
                requestRepository.delete(request.get());
            }
        }
    }
    public List<Request> getPendingRequests(String ownerToken) {
        ArrayList<Request> requests = new ArrayList<>();
        Optional<User> owner = userRepository.findByaccessToken(ownerToken);
        if (owner.isEmpty()) return requests;

        for (Request request : requestRepository.findByOwner(owner.get())) {
            if (request.isPending()) {
                requests.add(request);
            }
        }
        return requests;
    }
}
