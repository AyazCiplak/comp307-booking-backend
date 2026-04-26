//Programmed by Mao Yurun
package comp307.backend.account;

import comp307.backend.Exceptions.BadRequestException;
import comp307.backend.account.Object.User;
import comp307.backend.account.Object.UserRepository;
import comp307.backend.account.auth.AuthHelper;
import comp307.backend.booking.Entity.Booking;
import comp307.backend.booking.Entity.BookingSlot;
import comp307.backend.booking.Repository.BookingRepository;
import comp307.backend.booking.Repository.BookingSlotRepository;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
@Service
public class AccountService {
    final UserRepository userRepository;
    private final BookingSlotRepository bookingSlotRepository;
    private final BookingRepository bookingRepository;

    public AccountService(UserRepository userRepository, BookingSlotRepository bookingSlotRepository, BookingRepository bookingRepository) {
        this.userRepository = userRepository;
        this.bookingSlotRepository = bookingSlotRepository;
        this.bookingRepository = bookingRepository;
    }
    // TODO integrate with bookings
    public ArrayList<User> getFreeSlotOwners(String token) {
        if (userRepository.findById(token).isEmpty()) return null;
        ArrayList<User> owners = new ArrayList<>();

        for (User user : userRepository.findAll()) {
            if (user.isOwner()) {;
                userLoop:
                for (BookingSlot bookingSlot : bookingSlotRepository.findByOwner(user)) {
                    if (bookingSlot.getSlotStatus() == BookingSlot.BookingSlotStatus.AVAILABLE) {
                        for (Booking booking : bookingRepository.findByBookingSlot(bookingSlot)) {
                            if (booking.getReservee() == null) {

                                break userLoop;
                            }
                        }
                    }
                }
            }
        }

        return owners;
    }

    public User register(String email, String password) {
        // email has not been used
        if (isRegistered(email)) {
            throw new BadRequestException(email + " has already been registered");
        }

        User user = new User(email, password, "", "", generateToken());
        userRepository.save(user);

        return user;
    }

    public User login(String email, String password) {
        // user is registered
        if (!isRegistered(email)) {
            throw new BadRequestException("Account Not Found");
        }

        User user = userRepository.findById(email).get();

        // password is correct, comparing hashed values directly for better security
        if (user.getPassword().equals(AuthHelper.hashSHA256(password))) {
            // updates token on login to prevent old sessions being able to access
            user.updateToken(generateToken());
            return user;
        }

        throw new BadRequestException("Incorrect Password");
    }

    // TODO integrate with bookings and requests
    public List<Booking> listBooked(String token) {
        Optional<User> user = userRepository.findByaccessToken(token);

        return user.map(bookingRepository::findByReservee).orElse(null);
    }
    public void logout(String token) {
        User user = userRepository.findByToken(token);
        user.logout();
    }
    private boolean isRegistered(String email) {
        return userRepository.findById(email).isPresent();
    }
    private String generateToken() {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder sb = new StringBuilder();
        SecureRandom random = new SecureRandom();

        // no duplicates
        while (sb.isEmpty() || userRepository.findByaccessToken(sb.toString()).isPresent()) {
            for (int i = 0; i < 20; i++) {
                int index = random.nextInt(characters.length());
                sb.append(characters.charAt(index));
            }
        }
        return sb.toString();
    }
}
