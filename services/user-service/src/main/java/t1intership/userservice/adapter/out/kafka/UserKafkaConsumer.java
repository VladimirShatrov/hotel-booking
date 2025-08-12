package t1intership.userservice.adapter.out.kafka;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;
import t1internship.authservice.dto.UserKafkaData;
import t1intership.userservice.dto.UserData;
import t1intership.userservice.port.in.UserInPort;

@Component
@RequiredArgsConstructor
public class UserKafkaConsumer {

    private final UserInPort inPort;

    @KafkaListener(
        topics = "register-user", groupId = "user-service-group"
    )
    public void consume(@Payload UserKafkaData data) {
        UserData user = new UserData(data.id(), null, null, data.email(), null, null);
        inPort.save(user);
    }
}
