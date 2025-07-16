package entelect.training.incubator.spring.booking.model.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class BookingSearchRequest {
    private SearchType searchType;
    private String referenceNumber;
    private Integer customerId;
    private Integer flightId;
}
