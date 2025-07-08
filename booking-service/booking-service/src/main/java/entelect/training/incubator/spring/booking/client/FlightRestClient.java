package entelect.training.incubator.spring.booking.client;

import entelect.training.incubator.spring.booking.client.dto.Flight;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.Base64;

@Component
public class FlightRestClient {

    private final RestClient restClient;

    String username = "user";
    String password = "{noop}the_cake";
    String encodedCredentials = Base64.getEncoder().encodeToString((username + ":" + password).getBytes());

    public FlightRestClient() {
        restClient = RestClient.builder()
                .baseUrl("http://localhost:8202")
                .defaultHeader("Authorization", "Basic " + encodedCredentials)
                .build();
    }

    public Flight getFlightById(Integer flightId) {
        return restClient.get()
                .uri("/flights/{flightId}", flightId)
                .retrieve()
                .body(Flight.class);
    }
}
