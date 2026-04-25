package comp307.backend.account;

import comp307.backend.account.Object.User;
import comp307.backend.account.Object.UserRepository;
import comp307.backend.booking.Entity.Booking;
import comp307.backend.booking.Entity.BookingSlot;
import comp307.backend.booking.Repository.BookingRepository;
import comp307.backend.booking.Repository.BookingSlotRepository;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class AccountService {
    private final UserRepository userRepository;
    private final BookingSlotRepository bookingSlotRepository;
    private final BookingRepository bookingRepository;
    private final JavaMailSender mailSender;

    public AccountService(UserRepository userRepository, BookingSlotRepository bookingSlotRepository, BookingRepository bookingRepository, JavaMailSender mailSender) {
        this.userRepository = userRepository;
        this.bookingSlotRepository = bookingSlotRepository;
        this.bookingRepository = bookingRepository;
        this.mailSender = mailSender;
    }
    public ArrayList<User> getFreeSlotOwners() {
        ArrayList<User> owners = new ArrayList<>();

        for (User user : userRepository.findAll()) {
            if (user.isOwner()) {;

                //TODO add an indicator field in bookingSlot
                userLoop:
                for (BookingSlot bookingSlot : bookingSlotRepository.findByOwner(user)) {
                    if (bookingSlot.isActivated()) {
                        for (Booking booking : bookingRepository.findBySlotReserved(bookingSlot)) {
                            if (booking.getReservee() == null) {
                                owners.add(user);
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
    public ArrayList<BookingSlot> getSlots(String targetEmail) {
        ArrayList<BookingSlot> bookingSlots = new ArrayList<>();
        Optional<User> target = userRepository.findById(targetEmail);

        if (target.isPresent()) {
            User owner = target.get();

            for (BookingSlot bookingSlot : bookingSlotRepository.findByOwner(owner)) {
                if (bookingSlot.isActivated()) {
                    for (Booking booking : bookingRepository.findBySlotReserved(bookingSlot)) {
                        if (!bookingSlots.contains(bookingSlot) && booking.getReservee() == null) {
                            bookingSlots.add(bookingSlot);
                            break;
                        }
                    }
                }
            }
        }

        return bookingSlots;
    }

    public List<BookingSlot> getAllSlots(String targetEmail) {
        ArrayList<BookingSlot> bookingSlots = new ArrayList<>();
        Optional<User> target = userRepository.findById(targetEmail);

        if (target.isPresent()) {
            User owner = target.get();

            return bookingSlotRepository.findByOwner(owner);
        }

        return bookingSlots;
    }

    public BookingSlot createSlot(String ownerEmail, LocalDateTime startTime, LocalDateTime endTime) {
        User owner = getUser(ownerEmail);
        if (owner == null) return null;

        BookingSlot bookingSlot = new BookingSlot(owner, startTime, endTime);
        bookingSlotRepository.save(bookingSlot);

        return bookingSlot;
    }

    public String setSlotState(String ownerEmail, LocalDateTime startTime, LocalDateTime endTime) {
        User owner = getUser(ownerEmail);
        if (owner == null) return "";
        List<BookingSlot> queryResult = bookingSlotRepository.findByOwner(owner);
        for (BookingSlot bookingSlot : queryResult) {
            if (bookingSlot.getStart().equals(startTime) && bookingSlot.getEnd().equals(endTime)) {
                bookingSlot.activate();
                return "";
            }
        }
        // TODO return message
        return "";
    }
    public List<Booking> listBooked(String email) {
        User user = getUser(email);
        if (user == null) return null;
        return bookingRepository.findByReservee(user);
    }

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
    private boolean isRegistered(String email) {
        return userRepository.findById(email).isPresent();
    }

    private User getUser(String email) {
        Optional<User> queryResult = userRepository.findById(email);
        if (queryResult.isPresent()) {
            return queryResult.get();
        }
        return null;
    }
}
