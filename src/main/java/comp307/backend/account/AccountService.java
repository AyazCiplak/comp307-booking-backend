//Programmed by Mao Yurun
package comp307.backend.account;

import comp307.backend.Exceptions.BadRequestException;
import comp307.backend.account.Object.User;
import comp307.backend.account.Object.UserRepository;
import comp307.backend.account.auth.AuthHelper;
import comp307.backend.booking.Entity.Booking;
import comp307.backend.booking.Entity.BookingSlot;
import comp307.backend.booking.Entity.BookingsInterface;
import comp307.backend.booking.Entity.Request;
import comp307.backend.booking.Repository.BookingRepository;
import comp307.backend.booking.Repository.BookingSlotRepository;
import comp307.backend.booking.Repository.RequestRepository;
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
    private final RequestRepository requestRepository;

    public AccountService(UserRepository userRepository, BookingSlotRepository bookingSlotRepository, BookingRepository bookingRepository, RequestRepository requestRepository) {
        this.userRepository = userRepository;
        this.bookingSlotRepository = bookingSlotRepository;
        this.bookingRepository = bookingRepository;
        this.requestRepository = requestRepository;
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
    public ArrayList<User> getFreeSlotOwners(String token) {
        User caller = userRepository.findByToken(token);
        ArrayList<User> owners = new ArrayList<>();


        for (User user : userRepository.findAll().stream().filter(User::isOwner).toList()) {
            // you shouldn't see yourself when finding people to book
            if (user.equals(caller)) continue;

            for (BookingSlot bookingSlot : bookingSlotRepository.findByOwner(user)) {
                if (bookingSlot.getSlotStatus() == BookingSlot.BookingSlotStatus.AVAILABLE) {
                    owners.add(user);
                    break;
                }
            }
        }

        return owners;
    }
    public List<BookingsInterface> listBooked(String token) {
        User user = userRepository.findByToken(token);

        ArrayList<BookingsInterface> bookings = new ArrayList<>();
        bookings.addAll(requestRepository.findByRequester(user));

        for (Booking booking : bookingRepository.findByReservee(user)) {
            BookingSlot bookingSlot = booking.getBookingSlot();
            if (bookingSlot.getSlotStatus() != BookingSlot.BookingSlotStatus.CANCELLED) {
                bookings.add(bookingSlot);
            }
        }

        return bookings;
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
