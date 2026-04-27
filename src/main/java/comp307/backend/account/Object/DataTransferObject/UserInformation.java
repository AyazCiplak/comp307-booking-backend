// Programmed by Henry Niedermayer
package comp307.backend.account.Object.DataTransferObject;

import comp307.backend.account.Object.User;

/**
 * Safe outbound DTO for information about others Users. Excludes both password and access token.
 */
public class UserInformation {
    private final String email;
    private final String firstName;
    private final String lastName;
    private final boolean isOwner;
    private final String department;
    private final String title;

    public UserInformation(User user) {
        this.email = user.getEmail();
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
        this.isOwner = user.isOwner();
        this.department = user.getDepartment();
        this.title = user.getTitle();
    }

    public String getEmail() { return email; }
    public String getFirstName() { return firstName; }
    public String getLastName() { return lastName; }
    public boolean isOwner() { return isOwner; }
    public String getDepartment() { return department; }
    public String getTitle() { return title; }
}
