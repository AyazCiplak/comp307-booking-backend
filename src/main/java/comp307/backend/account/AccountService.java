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
    public ArrayList<User> getFreeSlotOwners() {
        ArrayList<User> owners = new ArrayList<>();

        for (User user : userRepository.findAll()) {
            if (user.isOwner()) {;

                //TODO add an indicator field in bookingSlot
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

    /*
    Dont need to send email on backend
    public void message(String senderEmail, String receiverEmail, String message) {
        User sender = getUser(senderEmail);
        User receiver = getUser(receiverEmail);
        if (sender == null || receiver == null) return;

        sendSimpleEmail(mailSender, receiverEmail, sender.getFirstName() + " " + sender.getLastName() + "has sent you a message", message);
    }

    // TODO move helper functions to somewhere better
    public static void sendSimpleEmail(JavaMailSender mailSender, String to, String subject, String body) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(body);
        mailSender.send(message);
    }
    */
    private boolean isRegistered(String email) {
        return userRepository.findById(email).isPresent();
    }
}
