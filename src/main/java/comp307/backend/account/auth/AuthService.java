//Programmed by Henry Niedermayer

package comp307.backend.account.auth;

import org.springframework.stereotype.Service;

import comp307.backend.Exceptions.AuthFailureException;
import comp307.backend.account.Object.User;
import comp307.backend.account.Object.UserRepository;

@Service
public class AuthService {
    private final UserRepository userRepository;

    public AuthService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User authenticate(String token) {
        return userRepository.findByAccessToken(token).orElseThrow(() -> new AuthFailureException());
    }
}
