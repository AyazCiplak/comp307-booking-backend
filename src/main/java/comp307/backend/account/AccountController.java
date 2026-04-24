package comp307.backend.account;

import comp307.backend.account.Object.User;
import comp307.backend.booking.Entity.Booking;
import comp307.backend.booking.Entity.BookingSlot;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
// TODO handle failures to process from calls
@RestController
@RequestMapping("api/account")
public class AccountController {
    private final AccountService service;
    public AccountController(AccountService service) {
        this.service = service;
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
    public ResponseEntity<List<User>> getFreeSlotOwners() {
        return ResponseEntity.ok(service.getFreeSlotOwners());
    }

    // callerEmail = targetEmail -> owner viewing their own slots
    // otherwise is someone else viewing an owner's available slots (activated and free)
    @PostMapping("/getSlots")
    public ResponseEntity<List<BookingSlot>> getSlots(@RequestBody String callerEmail, @RequestBody String targetEmail) {
        return ResponseEntity.ok(service.getSlots(callerEmail, targetEmail));
    }

    @PostMapping("/createSlot")
    public ResponseEntity<BookingSlot> createSlot(@RequestBody String ownerEmail, @RequestBody LocalDateTime startTime, @RequestBody LocalDateTime endTime) {
        return ResponseEntity.ok(service.createSlot(ownerEmail, startTime, endTime));
    }

    @PostMapping("/activate")
    public ResponseEntity<String> activate(@RequestBody String ownerEmail, @RequestBody LocalDateTime startTime, @RequestBody LocalDateTime endTime) {
        return ResponseEntity.ok(service.setSlotState(ownerEmail, startTime, endTime));
    }

    @PostMapping("/listBooked")
    public ResponseEntity<List<Booking>> listBooked(@RequestBody String email) {
        return ResponseEntity.ok(service.listBooked(email));
    }

    @PostMapping("/message")
    public ResponseEntity<Void> message(@RequestBody String senderEmail, @RequestBody String receiver, @RequestBody String message) {
        service.message(senderEmail, receiver, message);
        return ResponseEntity.noContent().build();
    }

    // TODO Users can logout
}
