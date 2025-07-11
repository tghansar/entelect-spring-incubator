package entelect.training.incubator.spring.booking.restclient;

import entelect.training.incubator.spring.booking.restclient.dto.Customer;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.Base64;

@Component
public class CustomerRestClient {

    private final RestClient restClient;

    String username = "admin";
    String password = "is_a_lie";
    String encodedCredentials = Base64.getEncoder().encodeToString((username + ":" + password).getBytes());

    public CustomerRestClient() {
        restClient = RestClient.builder()
                .baseUrl("http://localhost:8201")
                .defaultHeader("Authorization", "Basic " + encodedCredentials)
                .build();
    }

    public Customer getCustomersById(Integer customerId) {
        return restClient.get()
                .uri("/customers/{customerId}", customerId)
                .retrieve()
                .body(Customer.class);
    }
}
