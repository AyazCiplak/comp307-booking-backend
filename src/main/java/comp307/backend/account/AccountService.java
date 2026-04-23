package comp307.backend.account;

import comp307.backend.account.Object.Owner;
import comp307.backend.account.Object.User;
import comp307.backend.account.Object.UserRepository;
import comp307.backend.booking.Entity.BookingSlot;
import comp307.backend.booking.Service.BookingServiceold;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class AccountService {
    //TODO limit access
    public static UserRepository userRepository;
    @Autowired
    public static JavaMailSender mailSender;
    public AccountService(UserRepository userRepository) {
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
        if (!isRegistered(userRepository, email)) {
            User user;

            if(isOwner(email)) {
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
        if (isRegistered(userRepository, email)) {
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

    public ArrayList<BookingSlot> getSlots(String callerEmail, String targetEmail) {
        ArrayList<BookingSlot> bookingSlots = new ArrayList<>();
        Optional<User> caller = userRepository.findById(callerEmail);
        Optional<User> target = userRepository.findById(targetEmail);

        if (caller.isPresent() && target.isPresent()) {
            User user = caller.get();
            Owner owner = (Owner) target.get();

            bookingSlots = owner.getBookingSlots(user);
        }

        return bookingSlots;
    }

    public BookingSlot createSlot(String ownerEmail, int beginHour, int beginMinute, int endHour, int endMinute) {
        Owner owner = getOwner(userRepository, ownerEmail);
        if (owner == null) return null;
        return owner.createBookingSlot(beginHour, beginMinute, endHour, endMinute);
    }

    public String setSlotState(String ownerEmail, int beginHour, int beginMinute, int endHour, int endMinute) {
        Owner owner = getOwner(userRepository, ownerEmail);
        if (owner == null) return null;
        if (owner.getBookingSlots(owner).contains(new BookingSlot(owner, beginHour, beginMinute, endHour, endMinute))) {
            Optional<BookingSlot> queryResult = BookingServiceold.bookingRepository.findById(new BookingPK(owner, new TimeInterval(beginHour, beginMinute, endHour, endMinute)));
            if (queryResult.isPresent()) {
                BookingSlot bookingSlot = queryResult.get();
                bookingSlot.activate();
            }
        }
        return "";
    }
    public List<BookingSlot> listBooked(String email) {
        User user = getUser(userRepository, email);
        if (user == null) return null;
        return BookingServiceold.bookingRepository.findByReservee(user);
    }

    public void message(String senderEmail, String receiverEmail, String message) {
        User sender = getUser(userRepository, senderEmail);
        User receiver = getUser(userRepository, receiverEmail);
        if (sender == null || receiver == null) return;

        sendSimpleEmail(receiverEmail, sender.getFirstName() + " " + sender.getLastName() + "has sent you a message", message);
    }

    // TODO find a better place to hold helper functions
    public static void sendSimpleEmail(String to, String subject, String body) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(body);
        mailSender.send(message);
    }
    private boolean isRegistered(UserRepository userRepository, String email) {
        return userRepository.findById(email).isPresent();
    }
    private boolean isOwner(String email) {
        return email.contains("@mcgill.ca");
    }
    private Owner getOwner(UserRepository userRepository, String email) {
        Optional<User> queryResult = userRepository.findById(email);
        if (queryResult.isPresent()) {
            return (Owner) queryResult.get();
        }
        return null;
    }

    private User getUser(UserRepository userRepository, String email) {
        Optional<User> queryResult = userRepository.findById(email);
        if (queryResult.isPresent()) {
            return queryResult.get();
        }
        return null;
    }
}
