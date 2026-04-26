//Programmed by Henry Niedermayer

package comp307.backend.booking.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import comp307.backend.booking.Entity.MeetingSequence;

@Repository
public interface MeetingSequenceRepository extends JpaRepository<MeetingSequence, Long> {
    
}
