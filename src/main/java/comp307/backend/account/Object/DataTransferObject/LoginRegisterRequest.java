package comp307.backend.account.Object.DataTransferObject;

public class LoginRegisterRequest {
    private String email;
    private String password;
    public LoginRegisterRequest() {}

    public LoginRegisterRequest(String email, String password) {
        this.email = email;
        this.password = password;
    }
    public String getEmail() {
        return email;
    }
    public String getPassword() {
        return password;
    }
}
