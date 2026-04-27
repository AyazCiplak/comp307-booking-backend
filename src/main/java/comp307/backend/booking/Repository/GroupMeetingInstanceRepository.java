//Programmed by Henry Niedermayer

package comp307.backend.booking.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import comp307.backend.booking.Entity.GroupMeetingInstance;

@Repository
public interface GroupMeetingInstanceRepository extends JpaRepository<GroupMeetingInstance, Long> {
    List<GroupMeetingInstance> findByInviteToken(String inviteToken);
}
