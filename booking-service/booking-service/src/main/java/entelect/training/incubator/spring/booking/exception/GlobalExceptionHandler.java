package entelect.training.incubator.spring.booking.exception;

import entelect.training.incubator.spring.booking.model.dto.ErrorMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> generalExceptionHandler(Exception e) {

        log.error(e.getMessage(), e);

        return new ResponseEntity<>(e.getMessage(), INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(FlightNotFoundException.class)
    public ResponseEntity<ErrorMessage> flightNotFoundExceptionHandler(FlightNotFoundException e) {

        log.error(e.getMessage(), e);

        ErrorMessage errorMessage = new ErrorMessage();
        errorMessage.setMessage(e.getMessage());
        return new ResponseEntity<>(errorMessage, NOT_FOUND);
    }

    @ExceptionHandler(CustomerNotFound.class)
    public ResponseEntity<ErrorMessage> customerNotFoundExceptionHandler(CustomerNotFound e) {

        log.error(e.getMessage(), e);

        ErrorMessage errorMessage = new ErrorMessage();
        errorMessage.setMessage(e.getMessage());
        return new ResponseEntity<>(errorMessage, NOT_FOUND);
    }
}
