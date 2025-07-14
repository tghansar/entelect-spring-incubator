package entelect.training.incubator.spring.booking.soapclient;

import entelect.training.incubator.spring.booking.soapclient.model.CaptureRewardsRequest;
import entelect.training.incubator.spring.booking.soapclient.model.CaptureRewardsResponse;
import entelect.training.incubator.spring.booking.soapclient.model.RewardsBalanceRequest;
import entelect.training.incubator.spring.booking.soapclient.model.RewardsBalanceResponse;
import org.springframework.stereotype.Component;
import org.springframework.ws.client.core.WebServiceTemplate;

import java.math.BigDecimal;

/**
 * RewardsClient is a SOAP client for interacting with the Loyalty Service.
 * It provides methods to capture rewards and retrieve rewards balance.
 */
@Component
public class RewardsClient {

//    private static final String NAMESPACE_URI = "http://entelect.training/incubator/spring-loyalty-service";
    private static final String NAMESPACE_URI = "http://localhost:8208/ws";

    private final WebServiceTemplate webServiceTemplate;

    public RewardsClient(WebServiceTemplate webServiceTemplate) {
        this.webServiceTemplate = webServiceTemplate;
    }

    public BigDecimal captureRewards(String passportNumber, BigDecimal amount) {
        CaptureRewardsRequest request = new CaptureRewardsRequest();
        request.setPassportNumber(passportNumber);
        request.setAmount(amount);

        CaptureRewardsResponse response = (CaptureRewardsResponse) webServiceTemplate.marshalSendAndReceive(
                NAMESPACE_URI + "/captureRewardsRequest", request);

        return response.getBalance();
    }

    public BigDecimal getRewardsBalance(String passportNumber) {
        RewardsBalanceRequest request = new RewardsBalanceRequest();
        request.setPassportNumber(passportNumber);

        RewardsBalanceResponse response = (RewardsBalanceResponse) webServiceTemplate.marshalSendAndReceive(
                NAMESPACE_URI + "/rewardsBalanceRequest", request);

        return response.getBalance();
    }
}