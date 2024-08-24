package com.warehouse.service.Impl;

import com.warehouse.service.EventService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {


    @Value(value = "${spring.kafka.producer.topic}")
    private String producerTopic;

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @Override
    public void sendEvent(String message) {
        kafkaTemplate.send(producerTopic, message);
    }
}
