package comp307.backend.account.Object;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "Users")
public class User {
    @Column(name = "first_name")
    private String firstName;
    @Column(name = "last_name")
    private String lastName;
    @Id
    private String email;
    @Column(name = "password")
    private String password;
    @Column(name = "isOwner")
    private boolean isOwner;

    //empty constructor for JPA
    protected User() {}

    public User(String email, String password) {
        String nameSection = email.split("@")[0];

        this.firstName = nameSection.split("\\.")[0];
        this.lastName = nameSection.split("\\.")[nameSection.split("\\.").length-1];
        this.email = email;
        this.password = password;

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

    @Override
    public boolean equals(Object newUser) {
        if (newUser instanceof User) {
            return ((User) newUser).email.equals(this.email);
        }

        return false;
    }
}
