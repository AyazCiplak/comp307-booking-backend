//Programmed by Mao Yurun
package comp307.backend.account;

import comp307.backend.account.Object.DataTransferObject.LoginRequest;
import comp307.backend.account.Object.User;
import comp307.backend.booking.Entity.Booking;
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

    @PostMapping("/register")
    public ResponseEntity<User> register(@RequestBody LoginRequest combo) {
        return ResponseEntity.ok(accountService.register(combo.getEmail(), combo.getPassword()));
    }

    @PostMapping("/login")
    public ResponseEntity<User> login(@RequestBody LoginRequest combo) {
        return ResponseEntity.ok(accountService.login(combo.getEmail(), combo.getPassword()));
    }
    @PostMapping("/logout")
    public ResponseEntity<Void> logout(@RequestBody String token) {
        accountService.logout(token);
        return ResponseEntity.ok().build();
    }
    @PostMapping("/getFreeSlotOwners")
    public ResponseEntity<List<User>> getFreeSlotOwners(@RequestBody String token) {
        return ResponseEntity.ok(accountService.getFreeSlotOwners(token));
    }
    @PostMapping("/listBooked")
    public ResponseEntity<List<Booking>> listBooked(@RequestBody String token) {
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
