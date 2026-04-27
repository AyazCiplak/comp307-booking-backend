//Programmed by Mao Yurun
package comp307.backend.account;

import comp307.backend.account.Object.DataTransferObject.LoginRequest;
import comp307.backend.account.Object.DataTransferObject.UserResponse;
import comp307.backend.account.Object.User;
import comp307.backend.booking.Entity.Booking;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/account")
public class AccountController {
    private final AccountService accountService;

    public AccountController(AccountService service) {
        this.accountService = service;
    }

    /**
     * Register a new user.
     * Only @mcgill.ca and @mail.mcgill.ca addresses are accepted.
     * Returns a safe UserResponse (no password field).
     */
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody LoginRequest combo) {
        String email = combo.getEmail();

        // Enforce McGill-only registration
        if (email == null || (!email.endsWith("@mcgill.ca") && !email.endsWith("@mail.mcgill.ca"))) {
            return ResponseEntity.badRequest()
                    .body("Only McGill email addresses (@mcgill.ca or @mail.mcgill.ca) may register.");
        }

        User newUser = accountService.register(email, combo.getPassword());
        if (newUser != null) {
            return ResponseEntity.ok(new UserResponse(newUser));
        } else {
            return ResponseEntity.badRequest().body("An account with this email already exists.");
        }
    }

    /**
     * Log in an existing user.
     * Returns a safe UserResponse (no password field).
     */
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest combo) {
        User user = accountService.login(combo.getEmail(), combo.getPassword());
        if (user != null) {
            return ResponseEntity.ok(new UserResponse(user));
        } else {
            return ResponseEntity.badRequest().body("Invalid email or password.");
        }
    }

    /**
     * Returns all @mcgill.ca owners who have at least one available office-hours slot.
     * Safe UserResponse list (no passwords).
     */
    @GetMapping("/getFreeSlotOwners")
    public ResponseEntity<List<UserResponse>> getFreeSlotOwners() {
        List<UserResponse> owners = accountService.getFreeSlotOwners().stream()
                .map(UserResponse::new)
                .toList();
        return ResponseEntity.ok(owners);
    }

    /**
     * List all bookings for the given user email.
     * TODO: lock this down with auth — currently anyone can query anyone's bookings.
     */
    @GetMapping("/listBooked")
    public ResponseEntity<List<Booking>> listBooked(@RequestParam String email) {
        return ResponseEntity.ok(accountService.listBooked(email));
    }
}
