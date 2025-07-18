package entelect.training.incubator.spring.notification.kafka;

import lombok.Builder;

@Builder
public record MessageNotification(String phoneNumber, String message) {}
