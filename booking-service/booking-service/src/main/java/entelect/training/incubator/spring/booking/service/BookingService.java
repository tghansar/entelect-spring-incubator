package entelect.training.incubator.spring.booking.service;

import entelect.training.incubator.spring.booking.exception.CustomerNotFound;
import entelect.training.incubator.spring.booking.exception.FlightNotFoundException;
import entelect.training.incubator.spring.booking.kafka.KafkaProducer;
import entelect.training.incubator.spring.booking.kafka.MessageNotification;
import entelect.training.incubator.spring.booking.model.entity.Booking;
import entelect.training.incubator.spring.booking.model.dto.BookingSearchRequest;
import entelect.training.incubator.spring.booking.repository.BookingRepository;
import entelect.training.incubator.spring.booking.restclient.CustomerRestClient;
import entelect.training.incubator.spring.booking.restclient.FlightRestClient;
import entelect.training.incubator.spring.booking.restclient.dto.Customer;
import entelect.training.incubator.spring.booking.restclient.dto.Flight;
import entelect.training.incubator.spring.booking.soapclient.RewardsClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class BookingService {

    private final BookingRepository bookingRepository;
    private final CustomerRestClient customerRestClient;
    private final FlightRestClient flightRestClient;
    private final RewardsClient rewardsClient;
    private final KafkaProducer kafkaProducer;

    public Booking createBooking(BookingSearchRequest request) {

        Customer customer = customerRestClient.getCustomersById(request.getCustomerId());
        if(customer == null) {
            throw new CustomerNotFound("Customer with ID " + request.getCustomerId() + " does not exist");
        }

        Flight flight = flightRestClient.getFlightById(request.getFlightId());
        if(flight == null) {
            throw new FlightNotFoundException("Flight with ID " + request.getFlightId() + " does not exist");
        }

        rewardsClient.captureRewards(customer.getPassportNumber(), new BigDecimal(100));

        LocalDateTime currentTime = LocalDateTime.now();

        String smsMessage = buildMessageForKafka(customer, flight, currentTime);
        MessageNotification messageNotification = MessageNotification.builder()
                .phoneNumber(customer.getPhoneNumber())
                .message(smsMessage)
                .build();
        kafkaProducer.sendToKafka(messageNotification);

        Booking booking = new Booking();
        booking.setCustomerId(customer.getId());
        booking.setFlightId(request.getFlightId());
        booking.setBookingDate(currentTime.toString());
        booking.setStatus("ACTIVE");
        booking.setReferenceNumber(generateReferenceNo(currentTime));
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

    private String generateReferenceNo(LocalDateTime currentTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
        return currentTime.format(formatter) +
                (int) (Math.random() * 100);
    }

    private String buildMessageForKafka(Customer customer, Flight flight, LocalDateTime currentTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        return new StringBuilder()
                .append("Molo Air: Confirming flight ")
                .append(flight.getId())
                .append(" booked for ")
                .append(customer.getFirstName())
                .append(" ")
                .append(customer.getLastName())
                .append(" on ")
                .append(currentTime.format(formatter))
                .append(".")
                .toString();
    }
}
