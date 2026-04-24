package comp307.backend.account;

import comp307.backend.account.Object.Owner;
import comp307.backend.account.Object.User;
import comp307.backend.account.Object.UserRepository;
import comp307.backend.booking.Entity.Booking;
import comp307.backend.booking.Entity.BookingSlot;


import comp307.backend.booking.Repository.BookingRepository;
import comp307.backend.booking.Repository.BookingSlotRepository;
import comp307.backend.booking.Service.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
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
    public ArrayList<Owner> getFreeSlotOwners() {
        ArrayList<Owner> owners = new ArrayList<>();

        for (User user : userRepository.findAll()) {
            if (user.isOwner()) {
                Owner owner = (Owner) user;
                if (owner.hasAvailableSlots(bookingRepository, bookingSlotRepository)) {
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

            bookingSlots = owner.getBookingSlots(user, bookingRepository, bookingSlotRepository);
        }

        return bookingSlots;
    }

    public BookingSlot createSlot(String ownerEmail, LocalDateTime startTime, LocalDateTime endTime) {
        Owner owner = getOwner(userRepository, ownerEmail);
        if (owner == null) return null;
        return owner.createBookingSlot(bookingSlotRepository, startTime, endTime);
    }

    public String setSlotState(String ownerEmail, LocalDateTime startTime, LocalDateTime endTime) {
        Owner owner = getOwner(userRepository, ownerEmail);
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
        User user = getUser(userRepository, email);
        if (user == null) return null;
        return bookingRepository.findByReservee(user);
    }

    public void message(String senderEmail, String receiverEmail, String message) {
        User sender = getUser(userRepository, senderEmail);
        User receiver = getUser(userRepository, receiverEmail);
        if (sender == null || receiver == null) return;

        sendSimpleEmail(receiverEmail, sender.getFirstName() + " " + sender.getLastName() + "has sent you a message", message);
    }

    // TODO find a better place to hold helper functions
    public void sendSimpleEmail(String to, String subject, String body) {
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
