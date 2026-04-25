//Programmed by Henry Niedermayer

package comp307.backend.booking.Entity;

import java.time.LocalDateTime;

import comp307.backend.account.Object.User;
import jakarta.persistence.*;

@Entity
@Table(name = "MeetingSequences", uniqueConstraints = {@UniqueConstraint(columnNames = {"inviteToken"})})
public class MeetingSequence {
    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Long meetingSequenceID;

    @ManyToOne
    @JoinColumn(name = "ownerEmail", nullable = false)
    private User owner;

    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private int maxUsers;
    @Column(nullable = false)
    private String inviteToken;

    @Column(nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    //for JPA
    protected MeetingSequence() {}

    public MeetingSequence(User owner, String name, int maxUsers, String inviteToken) {
        this.owner = owner;
        this.name = name;
        this.maxUsers = maxUsers;
        this.inviteToken = inviteToken;
    }


    public Long getMeetingSequenceID() {
        return this.meetingSequenceID;
    }

    public User getOwner() {
        return this.owner;
    }

    public String getName() {
        return this.name;
    }

    public int getMaxUsers() {
        return this.maxUsers;
    }

    public String getInviteToken() {
        return this.inviteToken;
    }

    public LocalDateTime getCreatedAt() {
        return this.createdAt;
    }
}
