package entelect.training.incubator.spring.notification.config;

import entelect.training.incubator.spring.notification.kafka.MessageNotification;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import java.util.HashMap;
import java.util.Map;

@Configuration
@Slf4j
@EnableKafka
@Getter
@Setter
@ConfigurationProperties(prefix = "spring.kafka")
public class KafkaConsumerConfig {
    private String bootstrapServers;
    private String topicName;

//    @Bean
//    public ConsumerFactory<String, MessageNotification> consumerFactory() {
//        log.info("ConsumerFactory created with bootstrapServers: " + bootstrapServers);
//
//        Map<String, Object> configProps = new HashMap<>();
//        configProps.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
//        configProps.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
//        configProps.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
//        configProps.put(ConsumerConfig.GROUP_ID_CONFIG, "notification-service-group");
//
////        JsonDeserializer<MessageNotification> deserializer = new JsonDeserializer<>(MessageNotification.class);
////        deserializer.addTrustedPackages("entelect.training.incubator.spring.notification.kafka");
////        return new DefaultKafkaConsumerFactory<>(configProps, new StringDeserializer(), deserializer);
//        return new DefaultKafkaConsumerFactory<>(configProps);
//    }
//
//     @Bean
//     public ConcurrentKafkaListenerContainerFactory<String, MessageNotification> kafkaListenerContainerFactory() {
//         ConcurrentKafkaListenerContainerFactory<String, MessageNotification> factory =
//                 new ConcurrentKafkaListenerContainerFactory<>();
//         factory.setConsumerFactory(consumerFactory());
//         return factory;
//     }
@Bean
public ConsumerFactory<String, MessageNotification> consumerFactory() {
    JsonDeserializer<MessageNotification> deserializer = new JsonDeserializer<>(MessageNotification.class);
    deserializer.addTrustedPackages("*"); // or specify exact package

    Map<String, Object> props = new HashMap<>();
    props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
    props.put(ConsumerConfig.GROUP_ID_CONFIG, "notification-service-group");
    props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
    props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, deserializer);

    return new DefaultKafkaConsumerFactory<>(props, new StringDeserializer(), deserializer);
}

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, MessageNotification> kafkaListenerContainerFactory() {
        var factory = new ConcurrentKafkaListenerContainerFactory<String, MessageNotification>();
        factory.setConsumerFactory(consumerFactory());
        return factory;
    }
}
