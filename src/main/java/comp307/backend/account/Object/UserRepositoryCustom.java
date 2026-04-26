package comp307.backend.account.Object;

public interface UserRepositoryCustom {
    public User findByToken(String accessToken);
}
