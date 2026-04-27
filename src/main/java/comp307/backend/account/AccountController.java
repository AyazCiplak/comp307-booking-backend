//Programmed by Mao Yurun
package comp307.backend.account;

import comp307.backend.account.Object.DataTransferObject.LoginRequest;
import comp307.backend.account.Object.DataTransferObject.UserResponse;
import comp307.backend.account.Object.User;
import comp307.backend.booking.Entity.BookingsInterface;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.security.auth.login.AccountNotFoundException;
import javax.security.auth.login.FailedLoginException;
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
        }
        else {
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
        } 
        else {
            return ResponseEntity.badRequest().body("Invalid email or password.");
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(@RequestBody String token) {
        accountService.logout(token);
        return ResponseEntity.ok().build();
    }

    /**
     * Returns all @mcgill.ca owners who have at least one available office-hours slot.
     * Safe UserResponse list (no passwords).
     */
    @PostMapping("/getFreeSlotOwners")
    public ResponseEntity<List<UserResponse>> getFreeSlotOwners(@RequestBody String token) {
        List<UserResponse> owners = accountService.getFreeSlotOwners(token).stream()
                .map(UserResponse::new)
                .toList();

        return ResponseEntity.ok(owners);
    }

    /**
     * List all bookings for the given user email.
     */
    @PostMapping("/listBooked")
    public ResponseEntity<List<BookingsInterface>> listBooked(@RequestBody String token) {
        return ResponseEntity.ok(accountService.listBooked(token));
    }

    @ExceptionHandler(value = FailedLoginException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ResponseEntity<String> handleFailedLoginException(FailedLoginException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.FORBIDDEN);
    }
    @ExceptionHandler(value = AccountNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<String> handleAccountNotFoundException(AccountNotFoundException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
    }
}
