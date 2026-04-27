// Programmed by Ayaz Ciplak
package comp307.backend.account.Object.DataTransferObject;

import comp307.backend.account.Object.User;

/**
 * Safe outbound DTO for a logged-in / registered user.
 * Never includes the password field — the full User entity must not
 * be serialised directly to the client.
 */
public class UserResponse {
    private final String email;
    private final String firstName;
    private final String lastName;
    private final boolean isOwner;
    private final String department;
    private final String title;
    private final String accessToken;

    public UserResponse(User user) {
        this.email = user.getEmail();
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
        this.isOwner = user.isOwner();
        this.department = user.getDepartment();
        this.title = user.getTitle();
        this.accessToken = user.getAccessToken();
    }

    public String getEmail() { return email; }
    public String getFirstName() { return firstName; }
    public String getLastName() { return lastName; }
    public boolean isOwner() { return isOwner; }
    public String getDepartment() { return department; }
    public String getTitle() { return title; }
    public String getAccessToken() { return accessToken; }
}
