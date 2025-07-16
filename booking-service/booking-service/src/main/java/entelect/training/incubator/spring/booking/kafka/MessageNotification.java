package entelect.training.incubator.spring.booking.kafka;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Builder;

@Builder
@JsonSerialize
public record MessageNotification(String phoneNumber, String message) {}
