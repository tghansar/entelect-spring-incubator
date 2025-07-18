package entelect.training.incubator.spring.notification.sms.client;

import entelect.training.incubator.spring.notification.kafka.MessageNotification;

public interface SmsClient {

    void retrieveSmsMessage(MessageNotification messageNotification);
    
    void sendSms(String phoneNumber, String message);
}
