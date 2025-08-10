package t1intership.userservice.adapter.out.kafka;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;
import t1intership.userservice.dto.UserData;
import t1intership.userservice.port.in.UserInPort;

@Component
@RequiredArgsConstructor
public class UserKafkaConsumer {

    private static UserInPort inPort;

    @KafkaListener(
        topics = "register-user", groupId = "${KAFKA_GROUP_ID}"
    )
    public void consume(@Payload UserData data) {
        inPort.save(data);
    }
}
