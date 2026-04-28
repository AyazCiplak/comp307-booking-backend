//Programmed by Mao Yurun and Henry Niedermayer
package comp307.backend.account;

import comp307.backend.Exceptions.BadRequestException;
import comp307.backend.Exceptions.NotFoundException;
import comp307.backend.account.Object.User;
import comp307.backend.account.Object.UserRepository;
import comp307.backend.account.auth.AuthHelper;
import comp307.backend.account.auth.AuthService;
import comp307.backend.booking.Entity.Booking;
import comp307.backend.booking.Entity.BookingSlot;
import comp307.backend.booking.Entity.BookingsInterface;
import comp307.backend.booking.Repository.BookingRepository;
import comp307.backend.booking.Repository.BookingSlotRepository;
import comp307.backend.booking.Repository.RequestRepository;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;

@Service
public class AccountService {
    private final AuthService authService;
    private final UserRepository userRepository;
    private final BookingSlotRepository bookingSlotRepository;
    private final BookingRepository bookingRepository;
    private final RequestRepository requestRepository;

    public AccountService(AuthService authService, UserRepository userRepository, BookingSlotRepository bookingSlotRepository, BookingRepository bookingRepository, RequestRepository requestRepository) {
        this.authService = authService;
        this.userRepository = userRepository;
        this.bookingSlotRepository = bookingSlotRepository;
        this.bookingRepository = bookingRepository;
        this.requestRepository = requestRepository;
    }

    public User register(String email, String password, String department, String title) {
        // email has not been used
        if (isRegistered(email)) {
            throw new BadRequestException(email + " has already been registered");
        }

        User user = new User(email, password, department, title, generateToken());
        userRepository.save(user);

        return user;
    }

    public User login(String email, String password) {
        // user is registered
        if (!isRegistered(email)) {
            throw new NotFoundException("No account found for email: " + email);
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

    /**
     * Returns all @mcgill.ca owners who have at least one AVAILABLE OFFICE_HOURS slot.
     * Previously this always returned an empty list because owners.add(user) was never reached.
     */
    public ArrayList<User> getFreeSlotOwners(String token) {
        this.authService.authenticate(token);

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




    public List<BookingsInterface> listBooked(String token) {
        User user = this.authService.authenticate(token);

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
        User user = this.authService.authenticate(token);
        user.logout();
        userRepository.save(user);
    }

    private boolean isRegistered(String email) {
        return userRepository.findById(email).isPresent();
    }
    
    private String generateToken() {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder sb = new StringBuilder();
        SecureRandom random = new SecureRandom();

        // no duplicates
        while (sb.isEmpty() || userRepository.findByAccessToken(sb.toString()).isPresent()) {
            for (int i = 0; i < 20; i++) {
                int index = random.nextInt(characters.length());
                sb.append(characters.charAt(index));
            }
        }
        return sb.toString();
    }
}
