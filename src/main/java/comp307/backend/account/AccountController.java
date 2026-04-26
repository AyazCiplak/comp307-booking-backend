//Programmed by Mao Yurun
package comp307.backend.account;

import comp307.backend.account.Object.DataTransferObject.LoginRequest;
import comp307.backend.account.Object.User;
import comp307.backend.booking.Entity.Booking;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
// TODO handle failures to process from calls
@RestController
@RequestMapping("api/account")
public class AccountController {
    private final AccountService accountService;
    public AccountController(AccountService service) {
        this.accountService = service;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody LoginRequest combo) {
        User newUser = accountService.register(combo.getEmail(), combo.getPassword());
        if (newUser != null) {
            return ResponseEntity.ok(newUser);
        } else {
            return ResponseEntity.badRequest().body("User is registered");
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest combo) {
        User user = accountService.login(combo.getEmail(), combo.getPassword());
        if (user != null) {
            return ResponseEntity.ok(user);
        } else {
            return ResponseEntity.badRequest().body("Invalid email or password");
        }
    }

    @PostMapping("/getFreeSlotOwners")
    public ResponseEntity<List<User>> getFreeSlotOwners() {
        return ResponseEntity.ok(accountService.getFreeSlotOwners());
    }



    @PostMapping("/listBooked")
    public ResponseEntity<List<Booking>> listBooked(@RequestBody String email) {
        return ResponseEntity.ok(accountService.listBooked(email));
    }
}
