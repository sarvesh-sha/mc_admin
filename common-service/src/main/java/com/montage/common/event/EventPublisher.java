package com.montage.common.event;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class EventPublisher {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public void publishEvent(String topic, String key, Object event) {
        try {
            kafkaTemplate.send(topic, key, event)
                    .whenComplete((result, ex) -> {
                        if (ex == null) {
                            log.info("Event published successfully to topic: {}, partition: {}, offset: {}",
                                    topic, result.getRecordMetadata().partition(),
                                    result.getRecordMetadata().offset());
                        } else {
                            log.error("Failed to publish event to topic: " + topic, ex);
                        }
                    });
        } catch (Exception e) {
            log.error("Error publishing event to topic: " + topic, e);
            throw new EventPublishingException("Failed to publish event", e);
        }
    }
} 