package comp307.backend.account.auth;

import comp307.backend.account.Object.User;
import comp307.backend.account.Object.UserRepository;

import java.util.ArrayList;

public class AccountHelper {
    public static boolean isRegistered(UserRepository userRepository, String email) {
        return userRepository.findById(email).isPresent();
    }

    public static boolean isOwner(String email) {
        return email.contains("@mcgill.ca");
    }
}
