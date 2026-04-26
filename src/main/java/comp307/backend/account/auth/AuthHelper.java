// Programmed by Mao Yurun

package comp307.backend.account.auth;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HexFormat;

public class AuthHelper {
    public static String hashSHA256(String str) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] encodedHash = digest.digest(str.getBytes());

            String hexHash = HexFormat.of().formatHex(encodedHash);
            return hexHash;
        } catch (NoSuchAlgorithmException e) {}
        throw new RuntimeException("Failed to fetch Hashing algorithm");
    }
}
