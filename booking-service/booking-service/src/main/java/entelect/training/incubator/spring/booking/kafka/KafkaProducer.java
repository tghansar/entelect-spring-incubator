package entelect.training.incubator.spring.booking.kafka;

import entelect.training.incubator.spring.booking.config.KafkaProducerConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;

@Service
@Slf4j
@RequiredArgsConstructor
public class KafkaProducer {
    private final KafkaProducerConfig kafkaProducerConfig;
    private final KafkaTemplate<String, MessageNotification> kafkaTemplate;

    public void sendToKafka(MessageNotification messageNotification) {
        try {
            String currentTime = OffsetDateTime.now().toString();

            Message<MessageNotification> message = MessageBuilder
                    .withPayload(messageNotification)
                    .setHeader(KafkaHeaders.TOPIC, kafkaProducerConfig.getTopicName())
                    .setHeader(KafkaHeaders.KEY, currentTime)
                    .build();

            log.info("sending kafka message with payload: {}", messageNotification);
            kafkaTemplate.send(message);
            log.info("kafka message sent successfully");

        } catch (Exception e) {
            log.error("failed to send a kafka message: {}", e.getMessage(), e);
        }
    }
}