//Programmed by Mao Yurun
package comp307.backend.booking.Service;

import java.nio.file.AccessDeniedException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import comp307.backend.Exceptions.BadRequestException;
import comp307.backend.account.Object.User;
import comp307.backend.account.Object.UserRepository;
import comp307.backend.booking.Entity.Request;
import org.springframework.dao.PermissionDeniedDataAccessException;
import org.springframework.stereotype.Service;

import comp307.backend.booking.Repository.RequestRepository;

import javax.naming.NoPermissionException;

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
        User requester = userRepository.findByToken(requesterToken);
        User owner = userRepository.findById(ownerEmail).orElseThrow(() -> new NoSuchElementException("User " + ownerEmail + " not found"));

        if (owner.equals(requester)) {
            throw new IllegalArgumentException("You should not send a request to yourself");
        }

        if (!owner.isOwner()) {
            throw new BadRequestException(owner.getFirstName() + " " + owner.getLastName() + " is not an owner");
        }

        Request request = new Request(requester, owner, requestedStart, requestedEnd, message);
        requestRepository.save(request);

        return request;
    }

    public void setRequestState(Long requestID, String ownerToken, boolean accept) {
        Request request = requestRepository.findById(requestID).orElseThrow(() -> new NoSuchElementException("Request No." + requestID + "not found"));
        User owner = userRepository.findByToken(ownerToken);

        if (!request.isPending()) {
            throw new BadRequestException("Request No." + requestID +" is not pending");
        }

        if (!request.getOwner().equals(owner)) {
            throw new BadRequestException("You are not the owner of Request No." + requestID);
        }

        if (accept) {
            request.setStatus(true);
        } else {
            request.setStatus(false);
            requestRepository.delete(request);
        }
    }
    public List<Request> getPendingRequests(String ownerToken) {
        ArrayList<Request> requests = new ArrayList<>();
        User owner = userRepository.findByToken(ownerToken);

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
}
