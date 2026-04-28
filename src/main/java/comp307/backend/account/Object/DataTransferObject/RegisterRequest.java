// Programmed by Ayaz Ciplak
package comp307.backend.account.Object.DataTransferObject;

/**
 * Inbound DTO for POST /api/account/register.
 * Extends the basic email+password pair with optional owner-only fields.
 * firstName and lastName are intentionally omitted (the backend derives
 * them from the email address)
 */
public class RegisterRequest {
    private String email;
    private String password;
    /** Only meaningful for @mcgill.ca (owner) accounts; empty string otherwise. */
    private String department = "";
    /** Only meaningful for @mcgill.ca (owner) accounts; empty string otherwise. */
    private String title = "";

    public RegisterRequest() {}

    public String getEmail() { return email; }
    public String getPassword() { return password; }
    public String getDepartment() { return department != null ? department : ""; }
    public String getTitle() { return title != null ? title : ""; }
}
