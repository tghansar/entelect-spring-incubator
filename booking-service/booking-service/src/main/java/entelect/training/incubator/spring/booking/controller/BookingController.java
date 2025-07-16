package entelect.training.incubator.spring.booking.controller;

import entelect.training.incubator.spring.booking.model.entity.Booking;
import entelect.training.incubator.spring.booking.model.dto.BookingSearchRequest;
import entelect.training.incubator.spring.booking.model.dto.SearchType;
import entelect.training.incubator.spring.booking.service.BookingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("bookings")
@Slf4j
@RequiredArgsConstructor
public class BookingController {

    private final BookingService bookingService;

    @PostMapping
    public ResponseEntity<?> createBooking(@RequestBody BookingSearchRequest request) {
        log.info("Processing booking creation request for booking={}", request);

        if (request.getCustomerId() == null) {
            log.warn("Customer id is null");
            return ResponseEntity.badRequest().body("Customer ID must be provided");
        }

        if (request.getFlightId() == null) {
            log.warn("Flight id is null");
            return ResponseEntity.badRequest().body("Flight ID must be provided");
        }

        Booking savedBooking = bookingService.createBooking(request);

        log.trace("Booking created");
        return new ResponseEntity<>(savedBooking, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getBookingById(@PathVariable Integer id) {
        log.info("Processing booking search request for booking id={}", id);
        Booking booking = bookingService.getBookingById(id);

        if (booking != null) {
            log.trace("Found booking");
            return new ResponseEntity<>(booking, HttpStatus.OK);
        }

        log.trace("Booking not found");
        return ResponseEntity.notFound().build();
    }

    @PostMapping("/search")
    public ResponseEntity<?> searchBookings(@RequestBody BookingSearchRequest request) {
        log.info("Processing booking search request for booking={}", request);

        SearchType searchType = request.getSearchType();

        if (searchType == null) {
            log.warn("Search type is null");
            return ResponseEntity.badRequest().body("Search type must be provided");
        }
        if (searchType == SearchType.CUSTOMER_ID_SEARCH) {
            if (request.getCustomerId() == null) {
                log.warn("Customer id is null");
                return ResponseEntity.badRequest().body("Customer ID must be provided for customer ID search");
            }
            var bookings = bookingService.getBookingsForCustomerId(request.getCustomerId());
            return new ResponseEntity<>(bookings, HttpStatus.OK);
        }
        else if (searchType == SearchType.REFERENCE_NUMBER_SEARCH) {
            if (request.getReferenceNumber() == null) {
                log.warn("Booking reference number is null");
                return ResponseEntity.badRequest().body("Booking reference number must be provided for booking reference search");
            }
            var bookings = bookingService.getBookingsForReferenceNo(request.getReferenceNumber());
            return new ResponseEntity<>(bookings, HttpStatus.OK);
        }
        log.trace("No bookings found");
        return ResponseEntity.notFound().build();
    }
}
