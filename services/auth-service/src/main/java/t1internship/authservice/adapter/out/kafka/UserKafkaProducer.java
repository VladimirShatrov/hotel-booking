package t1internship.authservice.adapter.out.kafka;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import t1internship.shared.dto.UserKafkaData;

@Component
@RequiredArgsConstructor
public class UserKafkaProducer {

    private final static String TOPIC = "register-user";
    private final KafkaTemplate<String, UserKafkaData> kafkaTemplate;

    public void sendUser(UserKafkaData user) {
        kafkaTemplate.send(TOPIC ,user);
    }
}
