//Programmed by Henry Niedermayer

package comp307.backend.booking.DTOs;

public class CreateGroupMeetingInstance {
    private String ownerToken;
    private String name;
    private int maxUsers;
    private String inviteToken;

    public String getOwnerToken() {
        return this.ownerToken;
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
}
