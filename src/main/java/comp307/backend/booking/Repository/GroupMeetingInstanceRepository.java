//Programmed by Henry Niedermayer

package comp307.backend.booking.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import comp307.backend.account.Object.User;
import comp307.backend.booking.Entity.GroupMeetingInstance;

@Repository
public interface GroupMeetingInstanceRepository extends JpaRepository<GroupMeetingInstance, Long> {
    /** Returns all group meeting instances owned by a given user. */
    List<GroupMeetingInstance> findByOwner(User owner);

    /** Returns instances with the given invite token (should always be exactly 1 due to unique constraint). */
    List<GroupMeetingInstance> findByInviteToken(String inviteToken);
}
