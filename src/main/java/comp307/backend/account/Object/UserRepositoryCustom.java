package comp307.backend.account.Object;

public interface UserRepositoryCustom {
    // Can be called by raw as auth is contained in the query
    public User findByToken(String accessToken);
}
