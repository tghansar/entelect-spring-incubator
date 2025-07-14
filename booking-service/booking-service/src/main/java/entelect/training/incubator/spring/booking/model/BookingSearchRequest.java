package entelect.training.incubator.spring.booking.model;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class BookingSearchRequest {
    private SearchType searchType;
    private Integer customerId;
    private Integer flightId;
    private String referenceNumber;
    private String passportNumber;
}
