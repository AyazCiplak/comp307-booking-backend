package comp307.backend.account.auth;

import java.util.ArrayList;

public class AccountHelper {
    public static boolean isRegistered(String email) {
        // TODO do the actual check when database is implemented
        return false;
    }

    public static boolean isOwner(String email) {
        return email.contains("@mcgill.ca");
    }

}
