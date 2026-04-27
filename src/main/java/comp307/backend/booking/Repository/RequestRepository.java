//Programmed by Mao Yurun
package comp307.backend.booking.Repository;

import comp307.backend.account.Object.User;
import comp307.backend.booking.Entity.Request;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RequestRepository extends JpaRepository<Request, Long> {
    List<Request> findByOwner(User owner);
    List<Request> findByRequester(User requester);
    List<Request> findByStatus(Request.RequestStatus status);
}
