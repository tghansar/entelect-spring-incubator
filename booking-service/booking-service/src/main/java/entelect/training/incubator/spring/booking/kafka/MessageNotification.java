package entelect.training.incubator.spring.booking.kafka;

import lombok.Builder;

@Builder
public record MessageNotification(String phoneNumber, String message) {}
