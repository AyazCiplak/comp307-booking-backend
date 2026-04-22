package comp307.backend.account;

import comp307.backend.account.Object.Owner;
import comp307.backend.account.Object.User;
import comp307.backend.account.Object.UserRepository;
import comp307.backend.account.auth.AccountHelper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class UserService {
    private UserRepository userRepository;
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    public ArrayList<Owner> getAllOwners() {
        // TODO use better data structure
        // TODO impl
        ArrayList<Owner> owners = new ArrayList<>();
        for (User user : userRepository.findAll()) {
            if (user.isOwner()) {
                owners.add((Owner) user);
            }
        }
        return owners;
    }

    public void register(String firstName, String lastName, String email, String password) {
        if (!AccountHelper.isRegistered(email)) {
            User user;
            if(AccountHelper.isOwner(email)) {
                user = new Owner(firstName, lastName, email, password);
            } else {
                user = new User(firstName, lastName, email, password);
            }
            // TODO return success msg
            userRepository.save(user);
        } else {
            // TODO return error msg
        }
    }

    private void login(String email, String password) {
        if (AccountHelper.isRegistered(email)) {
            // TODO find email, then compare password
        } else {
            // TODO return error msg
        }
    }
}
