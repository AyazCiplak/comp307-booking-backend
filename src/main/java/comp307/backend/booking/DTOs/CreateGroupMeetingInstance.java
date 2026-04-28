//Programmed by Henry Niedermayer

package comp307.backend.booking.DTOs;

public class CreateGroupMeetingInstance {
    private String ownerToken;
    private String name;
    private int maxUsers;
    private String inviteToken;

    public String getOwnerToken() { return this.ownerToken; }
    public void setOwnerToken(String ownerToken) { this.ownerToken = ownerToken; }

    public String getName() { return this.name; }
    public void setName(String name) { this.name = name; }

    public int getMaxUsers() { return this.maxUsers; }
    public void setMaxUsers(int maxUsers) { this.maxUsers = maxUsers; }

    public String getInviteToken() { return this.inviteToken; }
    public void setInviteToken(String inviteToken) { this.inviteToken = inviteToken; }
}
