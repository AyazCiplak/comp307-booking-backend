package comp307.backend.account.Object;

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
    @Column
    private String firstName;
    @Column
    private String lastName;
    // TODO needs to be hashed
    @Column
    private String password;
    @Column
    private boolean isOwner;
    @Column
    private String department;
    @Column
    private String title;
    @Column
    private Timestamp createdAt;
    //empty constructor for JPA
    protected User() {}
    public User(String email, String password, String department, String title) {
        String nameSection = email.split("@")[0];

        this.firstName = nameSection.split("\\.")[0];
        this.lastName = nameSection.split("\\.")[nameSection.split("\\.").length-1];
        this.email = email;
        this.password = password;
        this.department = department;
        this.title = title;
        this.createdAt = Timestamp.from(Instant.now());

        isOwner = email.endsWith("@mcgill.ca");
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    // TODO should be private security wise, narrow down API for access
    public String getEmail() {
        return email;
    }

    // TODO should be private security wise, narrow down API for access
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
    @Override
    public boolean equals(Object newUser) {
        if (newUser instanceof User) {
            return ((User) newUser).email.equals(this.email);
        }

        return false;
    }
}
