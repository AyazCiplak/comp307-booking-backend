package comp307.backend.account.Object.DataTransferObject;

public class EmailTokenRequest {
    private String email;
    private String token;
    public EmailTokenRequest() {}

    public EmailTokenRequest(String email, String token) {
        this.email = email;
        this.token = token;
    }
    public String getEmail() {
        return email;
    }
    public String getToken() {
        return token;
    }
}
