package comp307.backend.booking.DTOs;

public class CreateGroupMeetingInstance {
    private String ownerEmail;
    private String name;
    private int maxUsers;
    private String inviteToken;

    public String getOwnerEmail() {
        return this.ownerEmail;
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
