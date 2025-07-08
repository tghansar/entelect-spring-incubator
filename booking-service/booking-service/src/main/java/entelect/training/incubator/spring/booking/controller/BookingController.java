package entelect.training.incubator.spring.booking.controller;

import entelect.training.incubator.spring.booking.client.CustomerRestClient;
import entelect.training.incubator.spring.booking.client.FlightRestClient;
import entelect.training.incubator.spring.booking.model.Booking;
import entelect.training.incubator.spring.booking.model.BookingSearchRequest;
import entelect.training.incubator.spring.booking.model.SearchType;
import entelect.training.incubator.spring.booking.service.BookingService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("bookings")
@RequiredArgsConstructor
public class BookingController {

    private final Logger LOGGER = LoggerFactory.getLogger(BookingController.class);

    private final BookingService bookingService;
    private final CustomerRestClient customerRestClient;
    private final FlightRestClient flightRestClient;

    @PostMapping
    public ResponseEntity<?> createBooking(@RequestBody BookingSearchRequest request) {
        LOGGER.info("Processing booking creation request for booking={}", request);

        if (request.getCustomerId() == null) {
            LOGGER.warn("Customer id is null");
            return ResponseEntity.badRequest().body("Customer ID must be provided");
        }

        if (request.getFlightId() == null) {
            LOGGER.warn("Flight id is null");
            return ResponseEntity.badRequest().body("Flight ID must be provided");
        }

        if(customerRestClient.getCustomersById(request.getCustomerId()) == null) {
            LOGGER.warn("Customer with id={} not found", request.getCustomerId());
            return ResponseEntity.badRequest().body("Customer with ID " + request.getCustomerId() + " does not exist");
        }

        if(flightRestClient.getFlightById(request.getFlightId()) == null) {
            LOGGER.warn("Flight with id={} not found", request.getFlightId());
            return ResponseEntity.badRequest().body("Flight with ID " + request.getFlightId() + " does not exist");
        }

        Booking savedBooking = bookingService.createBooking(request);

        LOGGER.trace("Booking created");
        return new ResponseEntity<>(savedBooking, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getBookingById(@PathVariable Integer id) {
        LOGGER.info("Processing booking search request for booking id={}", id);
        Booking booking = bookingService.getBookingById(id);

        if (booking != null) {
            LOGGER.trace("Found booking");
            return new ResponseEntity<>(booking, HttpStatus.OK);
        }

        LOGGER.trace("Booking not found");
        return ResponseEntity.notFound().build();
    }

    @PostMapping("/search")
    public ResponseEntity<?> searchBookings(@RequestBody BookingSearchRequest request) {
        LOGGER.info("Processing booking search request for booking={}", request);

        SearchType searchType = request.getSearchType();

        if (searchType == null) {
            LOGGER.warn("Search type is null");
            return ResponseEntity.badRequest().body("Search type must be provided");
        }
        if (searchType == SearchType.CUSTOMER_ID_SEARCH) {
            if (request.getCustomerId() == null) {
                LOGGER.warn("Customer id is null");
                return ResponseEntity.badRequest().body("Customer ID must be provided for customer ID search");
            }
            var bookings = bookingService.getBookingsForCustomerId(request.getCustomerId());
            return new ResponseEntity<>(bookings, HttpStatus.OK);
        }
        else if (searchType == SearchType.REFERENCE_NUMBER_SEARCH) {
            if (request.getReferenceNumber() == null) {
                LOGGER.warn("Booking reference number is null");
                return ResponseEntity.badRequest().body("Booking reference number must be provided for booking reference search");
            }
            var bookings = bookingService.getBookingsForReferenceNo(request.getReferenceNumber());
            return new ResponseEntity<>(bookings, HttpStatus.OK);
        }
        LOGGER.trace("No bookings found");
        return ResponseEntity.notFound().build();
    }
}
