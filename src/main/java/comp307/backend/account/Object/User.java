//Programmed by Mao Yurun
package comp307.backend.account.Object;

import comp307.backend.account.auth.AuthHelper;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.sql.Timestamp;
import java.time.Instant;

@Entity
@Table(name = "Users")
public class User {
    @Id
    private String email;
    @Column(nullable = false)
    private String firstName;
    @Column(nullable = false)
    private String lastName;
    @Column(nullable = false)
    private String password;
    @Column(nullable = false)
    private boolean isOwner;
    @Column
    private String department;
    @Column
    private String title;
    @Column(nullable = false)
    private Timestamp createdAt;
    
    @Column(unique = true)
    private String accessToken;

    protected User() {}

    public User(String email, String password, String department, String title, String accessToken) {
        String nameSection = email.split("@")[0];

        this.firstName = nameSection.split("\\.")[0];
        this.lastName = nameSection.split("\\.")[nameSection.split("\\.").length-1];
        this.email = email;
        this.password = AuthHelper.hashSHA256(password); // does not store the raw password at all
        this.department = department;
        this.title = title;
        this.createdAt = Timestamp.from(Instant.now());
        this.accessToken = accessToken;

        isOwner = email.endsWith("@mcgill.ca");
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }
    public String getPassword() {
        return password;
    }
    public boolean isOwner() {
        return isOwner;
    }

    public String getDepartment() {
        return department;
    }
    public String getTitle() {
        return title;
    }
    public Timestamp getCreatedAt() {
        return createdAt;
    }
    public String getAccessToken() {
        return accessToken;
    }
    public void updateToken(String accessToken) {
        this.accessToken = accessToken;
    }
    public void logout() {
        this.accessToken = "";
    }
    @Override
    public boolean equals(Object newUser) {
        if (newUser instanceof User) {
            return ((User) newUser).email.equals(this.email);
        }

        return false;
    }
}
