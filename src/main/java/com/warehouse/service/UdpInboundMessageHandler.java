package com.warehouse.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.warehouse.dto.SensorDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.annotation.MessageEndpoint;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.messaging.Message;
import org.springframework.messaging.handler.annotation.Headers;

import java.util.Map;
import java.util.Objects;


@MessageEndpoint
public class UdpInboundMessageHandler {

    private final static Logger LOGGER = LoggerFactory.getLogger(UdpInboundMessageHandler.class);

    @Autowired
    EventService eventService;

    private ObjectMapper mapper = new ObjectMapper();

    @ServiceActivator(inputChannel = "temperature")
    public void handeTemperature(Message message, @Headers Map<String, Object> headerMap) throws JsonProcessingException {
        if (Objects.nonNull(message)) {
            LOGGER.debug("Received Temperature message: {}", message.getPayload());
            eventService.sendEvent(mapper.writeValueAsString(parseMessage(message.getPayload(), "temperature")));
        }
    }

    @ServiceActivator(inputChannel = "humidity")
    public void handleHumidity(Message message, @Headers Map<String, Object> headerMap) throws JsonProcessingException, JsonProcessingException {
        if (Objects.nonNull(message)) {
            LOGGER.debug("Received Humidity message: {}", message.getPayload());
            eventService.sendEvent(mapper.writeValueAsString(parseMessage(message.getPayload(), "humidity")));
        }
    }

    // Static method to parse from message string
    public static SensorDto parseMessage(Object message, String sensorType) {
        String sensorIdPart = null;
        int valuePart = 0;
        try {
            String[] parts = message.toString().split(";");
            sensorIdPart = parts[0].split("=")[1].trim();
            valuePart = Integer.parseInt(parts[1].split("=")[1].trim());
        } catch (Exception e) {
            LOGGER.error("Invalid message format:{} ", message, e);
        }
        return new SensorDto(sensorIdPart, valuePart, sensorType);
    }
}
