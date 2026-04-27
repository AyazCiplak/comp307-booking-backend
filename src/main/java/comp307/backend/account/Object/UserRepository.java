//Programmed by Mao Yurun
package comp307.backend.account.Object;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
    Optional<User> findByAccessToken(String accessToken);
}

