//Programmed by Mao Yurun
package comp307.backend.account;

import comp307.backend.account.Object.User;
import comp307.backend.account.Object.UserRepository;
import comp307.backend.booking.Entity.Booking;
import comp307.backend.booking.Entity.BookingSlot;
import comp307.backend.booking.Repository.BookingRepository;
import comp307.backend.booking.Repository.BookingSlotRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class AccountService {
    private final UserRepository userRepository;
    private final BookingSlotRepository bookingSlotRepository;
    private final BookingRepository bookingRepository;

    public AccountService(UserRepository userRepository, BookingSlotRepository bookingSlotRepository, BookingRepository bookingRepository) {
        this.userRepository = userRepository;
        this.bookingSlotRepository = bookingSlotRepository;
        this.bookingRepository = bookingRepository;
    }

    /**
     * Returns all @mcgill.ca owners who have at least one AVAILABLE OFFICE_HOURS slot.
     * Previously this always returned an empty list because owners.add(user) was never reached.
     */
    public ArrayList<User> getFreeSlotOwners() {
        ArrayList<User> owners = new ArrayList<>();

        for (User user : userRepository.findAll()) {
            if (user.isOwner()) {
                boolean hasAvailableSlot = bookingSlotRepository.findByOwner(user).stream()
                        .anyMatch(slot ->
                                slot.getSlotStatus() == BookingSlot.BookingSlotStatus.AVAILABLE
                                && slot.getSlotType() == BookingSlot.BookingSlotType.OFFICE_HOURS
                        );
                if (hasAvailableSlot) {
                    owners.add(user);
                }
            }
        }

        return owners;
    }

    public User register(String email, String password) {
        if (!isRegistered(email)) {
            User user = new User(email, password, "", "");
            userRepository.save(user);
            return user;
        }
        return null;
    }

    public User login(String email, String password) {
        if (isRegistered(email)) {
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

    public List<Booking> listBooked(String email) {
        Optional<User> user = userRepository.findById(email);
        return user.map(bookingRepository::findByReservee).orElse(null);
    }

    private boolean isRegistered(String email) {
        return userRepository.findById(email).isPresent();
    }
}
