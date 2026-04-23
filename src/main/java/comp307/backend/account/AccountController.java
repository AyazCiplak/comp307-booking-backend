package comp307.backend.account;

import comp307.backend.account.Object.Owner;
import comp307.backend.account.Object.User;
import comp307.backend.account.Object.UserRepository;
import comp307.backend.booking.Object.BookingSlot;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@RestController
@RequestMapping("api/account")
public class AccountController {
    private final UserService service;
    public AccountController(UserRepository userRepository) {
        this.service = new UserService(userRepository);
    }
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody String email, @RequestBody String password) {
        User newUser = service.register(email, password);
        if (newUser != null) {
            return ResponseEntity.ok(newUser);
        } else {
            return ResponseEntity.badRequest().body("User is registered");
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody String email, @RequestBody String password) {
        User user = service.login(email, password);
        if (user != null) {
            return ResponseEntity.ok(user);
        } else {
            return ResponseEntity.badRequest().body("Invalid email or password");
        }
    }

    @PostMapping("/getFreeSlotOwners")
    public ResponseEntity<ArrayList<Owner>> getFreeSlotOwners() {
        return ResponseEntity.ok(service.getFreeSlotOwners());
    }

    @PostMapping("/getSlots/{caller}:{target}")
    public ResponseEntity<ArrayList<BookingSlot>> getSlots(@PathVariable("caller") String callerEmail, @PathVariable("target") String targetEmail) {
        ArrayList<BookingSlot> bookingSlots = service.getSlots(callerEmail, targetEmail);
        if (bookingSlots.isEmpty()) {
            ResponseEntity.badRequest().body("Selected owner has no available booking slot");
        }
        return ResponseEntity.ok(bookingSlots);
    }
    // TODO Users can see all the slots they have booked

    // TODO Users can message the owner of the booking slot

    // TODO Users can logout

}
