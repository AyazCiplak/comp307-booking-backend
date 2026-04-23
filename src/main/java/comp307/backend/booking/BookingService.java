package comp307.backend.booking;

import comp307.backend.account.AccountService;
import comp307.backend.account.Object.Owner;
import comp307.backend.account.Object.User;
import comp307.backend.booking.Object.BookingPK;
import comp307.backend.booking.Object.BookingRepository;
import comp307.backend.booking.Object.BookingSlot;
import comp307.backend.booking.Object.TimeInterval;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class BookingService {
    //TODO limit access
    public static BookingRepository bookingRepository;

    public BookingService(BookingRepository bookingRepository) {
        this.bookingRepository = bookingRepository;
    }

    public BookingSlot book(String callerEmail, String targetEmail, int beginHour, int beginMinute, int endHour, int endMinute) {
        Optional<User> caller = AccountService.userRepository.findById(callerEmail);
        Optional<User> target = AccountService.userRepository.findById(targetEmail);

        if (caller.isPresent() && target.isPresent()) {
            User user = caller.get();
            Owner owner = (Owner) target.get();

            Optional<BookingSlot> slot = bookingRepository.findById(new BookingPK(owner, new TimeInterval(beginHour, beginMinute, endHour, endMinute)));

            if (slot.isPresent()) {
                slot.get().book(user);
                return slot.get();
            }
        }

        return null;
    }
    public void unbook(String callerEmail, String targetEmail, int beginHour, int beginMinute, int endHour, int endMinute) {
        Optional<User> caller = AccountService.userRepository.findById(callerEmail);
        Optional<User> target = AccountService.userRepository.findById(targetEmail);

        if (caller.isPresent() && target.isPresent()) {
            User user = caller.get();
            Owner owner = (Owner) target.get();

            Optional<BookingSlot> slot = bookingRepository.findById(new BookingPK(owner, new TimeInterval(beginHour, beginMinute, endHour, endMinute)));

            if (slot.isPresent()) {
                slot.get().unbook(user);
                AccountService.sendSimpleEmail(owner.getEmail(), "One of your booking slot is now free", "Reservee " + user.getFirstName() + " " + user.getLastName() + " has canceled their booking to your booking slot in " + slot.get().getTimeInterval().toString() + ", it is now free.");
            }
        }
    }

    public void delete(String ownerEmail, int beginHour, int beginMinute, int endHour, int endMinute) {
        Optional<User> ownerResult = AccountService.userRepository.findById(ownerEmail);

        if (ownerResult.isPresent()) {
            Owner owner = (Owner) ownerResult.get();

            Optional<BookingSlot> slot = bookingRepository.findById(new BookingPK(owner, new TimeInterval(beginHour, beginMinute, endHour, endMinute)));

            if (slot.isPresent()) {

                User reservee = slot.get().getReservee();
                if (reservee != null) {

                    AccountService.sendSimpleEmail(reservee.getEmail(), "One of your booking has been deleted", "Owner " + owner.getFirstName() + " " + owner.getLastName() + " has canceled deleted their booking slot in " + slot.get().getTimeInterval().toString());
                }

                bookingRepository.delete(slot.get());
            }
        }
    }


}
