package comp307.backend.account;

import comp307.backend.account.Object.Owner;
import comp307.backend.account.Object.User;
import comp307.backend.account.Object.UserRepository;
import comp307.backend.account.auth.AccountHelper;
import comp307.backend.booking.Object.Booking;
import comp307.backend.booking.Object.BookingSlot;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    public ArrayList<Owner> getFreeSlotOwners() {
        ArrayList<Owner> owners = new ArrayList<>();

        for (User user : userRepository.findAll()) {
            if (user.isOwner()) {
                Owner owner = (Owner) user;
                if (owner.hasAvailableSlots()) {
                    owners.add((Owner) user);
                }
            }
        }

        return owners;
    }

    public User register(String email, String password) {
        if (!AccountHelper.isRegistered(userRepository, email)) {
            User user;

            if(AccountHelper.isOwner(email)) {
                user = new Owner(email, password);
            } else {
                user = new User(email, password);
            }

            userRepository.save(user);
            return user;
        }

        return null;
    }

    public User login(String email, String password) {
        if (AccountHelper.isRegistered(userRepository, email)) {
            Optional<User> userField = userRepository.findById(email);

            // user is registered
            if (userField.isPresent()) {
                User user = userField.get();
                // password is correct
                if (user.getPassword().equals(password)) {
                    return user;
                }
            }
        }

        return null;
    }

    public ArrayList<BookingSlot> getSlots(String userEmail, String targetEmail) {
        ArrayList<BookingSlot> bookingSlots = new ArrayList<>();
        Optional<User> user = userRepository.findById(userEmail);
        Optional<User> target = userRepository.findById(targetEmail);

        if (user.isPresent() && target.isPresent()) {
            User caller = user.get();
            Owner owner = (Owner) target.get();

            bookingSlots = owner.getBookingSlots(caller);
        }

        return bookingSlots;
    }
}
