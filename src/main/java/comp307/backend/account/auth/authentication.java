package comp307.backend.account.auth;

import comp307.backend.account.Object.Owner;
import comp307.backend.account.Object.User;

public class authentication {
    public void register(String firstName, String lastName, String email, String password) {
        if (!AccountHelper.isRegistered(email)) {
            User user;
            if(AccountHelper.isOwner(email)) {
                user = new Owner(firstName, lastName, email, password);
            } else {
                user = new User(firstName, lastName, email, password);
            }
            // TODO return success msg
            // TODO return to database
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
