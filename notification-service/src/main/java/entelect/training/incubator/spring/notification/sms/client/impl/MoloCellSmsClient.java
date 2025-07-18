package entelect.training.incubator.spring.notification.sms.client.impl;

import entelect.training.incubator.spring.notification.kafka.MessageNotification;
import entelect.training.incubator.spring.notification.sms.client.SmsClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

/**
 * A custom implementation of a fictional SMS service.
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class MoloCellSmsClient implements SmsClient {

    @Override
    @KafkaListener(
            topics = "${spring.kafka.topic-name}",
            groupId = "notification-service-group",
            containerFactory = "kafkaListenerContainerFactory"
    )
    public void retrieveSmsMessage(MessageNotification messageNotification) {
        var phoneNumber = messageNotification.phoneNumber();
        var message = messageNotification.message();
        sendSms(phoneNumber, message);
    }

    @Override
    public void sendSms(String phoneNumber, String message) {
        log.info("Sending SMS, destination='{}', '{}'", phoneNumber, message);
    }
}
