package comp307.backend.account;

import comp307.backend.account.Object.Owner;
import comp307.backend.account.Object.User;
import comp307.backend.account.Object.UserRepository;
import comp307.backend.booking.Entity.BookingSlot;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
// TODO handle failures to process from calls
@RestController
@RequestMapping("api/account")
public class AccountController {
    private final AccountService service;
    public AccountController(UserRepository userRepository) {
        this.service = new AccountService(userRepository);
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
    public ResponseEntity<List<Owner>> getFreeSlotOwners() {
        return ResponseEntity.ok(service.getFreeSlotOwners());
    }

    // callerEmail = targetEmail -> owner viewing their own slots
    // otherwise is someone else viewing an owner's available slots (activated and free)
    @PostMapping("/getSlots")
    public ResponseEntity<List<BookingSlot>> getSlots(@RequestBody String callerEmail, @RequestBody String targetEmail) {
        return ResponseEntity.ok(service.getSlots(callerEmail, targetEmail));
    }

    @PostMapping("/createSlot")
    public ResponseEntity<BookingSlot> createSlot(@RequestBody String ownerEmail, @RequestBody int beginHour, @RequestBody int beginMinute, @RequestBody int endHour, @RequestBody int endMinute) {
        return ResponseEntity.ok(service.createSlot(ownerEmail, beginHour, beginMinute, endHour, endMinute));
    }

    @PostMapping("/activate")
    public ResponseEntity<String> activate(@RequestBody String ownerEmail, @RequestBody int beginHour, @RequestBody int beginMinute, @RequestBody int endHour, int endMinute) {
        return ResponseEntity.ok(service.setSlotState(ownerEmail, beginHour, beginMinute, endHour, endMinute));
    }

    @PostMapping("/listBooked")
    public ResponseEntity<List<BookingSlot>> listBooked(@RequestBody String email) {
        return ResponseEntity.ok(service.listBooked(email));
    }

    @PostMapping("/message")
    public ResponseEntity<Void> message(@RequestBody String senderEmail, @RequestBody String receiver, @RequestBody String message) {
        service.message(senderEmail, receiver, message);
        return ResponseEntity.ok(null);
    }

    // TODO Users can logout
}
