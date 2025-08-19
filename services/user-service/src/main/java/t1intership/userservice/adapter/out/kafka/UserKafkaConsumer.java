package t1intership.userservice.adapter.out.kafka;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;
import t1internship.shared.dto.UserKafkaData;
import t1intership.userservice.dto.UserData;
import t1intership.userservice.port.in.UserInPort;

@Component
@RequiredArgsConstructor
@Slf4j
public class UserKafkaConsumer {

    private final UserInPort inPort;

    @KafkaListener(
        topics = "register-user", groupId = "user-service-group"
    )
    public void consume(@Payload UserKafkaData data, @Header(KafkaHeaders.RECEIVED_KEY) String key) {
        log.info("RECEIVED MESSAGE - KEY: {}, DATA: {}", key, data);

        UserData user = new UserData(data.id(), null,
                null, data.email(), null, null);
        inPort.save(user);
    }
}
