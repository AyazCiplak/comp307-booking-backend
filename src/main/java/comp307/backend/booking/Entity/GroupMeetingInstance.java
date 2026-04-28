//Programmed by Henry Niedermayer

package comp307.backend.booking.Entity;

import java.time.LocalDateTime;

import comp307.backend.account.Object.User;
import jakarta.persistence.*;

@Entity
@Table(name = "GroupMeetingInstances", uniqueConstraints = {@UniqueConstraint(columnNames = {"inviteToken"})})
public class GroupMeetingInstance {
    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Long groupMeetingInstanceID;

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

    /** True once the owner has selected a final proposal slot via selectGroupMeetingProposalSlot. */
    @Column(nullable = false)
    private boolean finalized = false;

    //for JPA
    protected GroupMeetingInstance() {}

    public GroupMeetingInstance(User owner, String name, int maxUsers, String inviteToken) {
        this.owner = owner;
        this.name = name;
        this.maxUsers = maxUsers;
        this.inviteToken = inviteToken;
    }


    public Long getGroupMeetingInstanceID() {
        return this.groupMeetingInstanceID;
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

    public boolean isFinalized() {
        return this.finalized;
    }

    public void setFinalized(boolean finalized) {
        this.finalized = finalized;
    }
}
