package entelect.training.incubator.spring.booking.service;

import entelect.training.incubator.spring.booking.model.Booking;
import entelect.training.incubator.spring.booking.model.BookingSearchRequest;
import entelect.training.incubator.spring.booking.repository.BookingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BookingService {

    private final BookingRepository bookingRepository;

    public Booking createBooking(BookingSearchRequest request) {
        Booking booking = new Booking();
        booking.setCustomerId(request.getCustomerId());
        booking.setFlightId(request.getFlightId());
        booking.setBookingDate(LocalDateTime.now().toString());
        booking.setStatus("ACTIVE");
        booking.setReferenceNumber(generateReferenceNo());
        return bookingRepository.save(booking);
    }

    public Booking getBookingById(Integer id) {
        return bookingRepository.findById(id).orElse(null);
    }

    public List<Booking> getBookingsForCustomerId(Integer customerId) {
        List<Booking> bookings = bookingRepository.getBookingByCustomerId(customerId);
        if (bookings == null) {
            return Collections.emptyList();
        }
        return bookings;
    }

    public List<Booking> getBookingsForReferenceNo(String referenceNo) {
        List<Booking> bookings = bookingRepository.getBookingsByReferenceNumber(referenceNo);
        if (bookings.isEmpty()) {
            return Collections.emptyList();
        }
        return bookings;
    }

    private String generateReferenceNo() {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
        return now.format(formatter) +
                (int) (Math.random() * 100);
    }
}
