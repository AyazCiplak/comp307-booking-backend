package comp307.backend.account.Object;

import comp307.backend.Exceptions.AuthFailureException;

import java.util.Optional;

public class UserRepositoryImpl implements UserRepositoryCustom{
    UserRepository userRepository;
    public UserRepositoryImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    @Override
    public User findByToken(String accessToken) {
        Optional<User> queryResult = userRepository.findByaccessToken(accessToken);

        if (queryResult.isEmpty()) {
            throw new AuthFailureException();
        }

        return queryResult.get();
    }
}
