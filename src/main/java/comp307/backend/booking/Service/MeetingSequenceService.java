//Programmed by Henry Niedermayer

package comp307.backend.booking.Service;

import org.springframework.stereotype.Service;

import comp307.backend.account.Object.User;
import comp307.backend.account.Object.UserRepository;
import comp307.backend.booking.Entity.MeetingSequence;
import comp307.backend.booking.Repository.MeetingSequenceRepository;

@Service
public class MeetingSequenceService {
    private final MeetingSequenceRepository meetingSequenceRepository;
    private final UserRepository userRepository;

    public MeetingSequenceService(MeetingSequenceRepository meetingSequenceRepository, UserRepository userRepository) {
        this.meetingSequenceRepository = meetingSequenceRepository;
        this.userRepository = userRepository;
    }


    public MeetingSequence getMeetingSequenceByID(Long meetingSequenceID) {
        return meetingSequenceRepository.findById(meetingSequenceID).orElseThrow(() -> new RuntimeException("MeetingSequence with id " + meetingSequenceID + " not found."));
    }


    public MeetingSequence createMeetingSequence(String ownerEmail, String name, int maxUsers, String inviteToken) {
        User owner = userRepository.findById(ownerEmail).orElseThrow(() -> new RuntimeException("User " + ownerEmail + " not found."));

        MeetingSequence meetingSequence = new MeetingSequence(owner, name, maxUsers, inviteToken); 
        return meetingSequenceRepository.save(meetingSequence);
    }
}
