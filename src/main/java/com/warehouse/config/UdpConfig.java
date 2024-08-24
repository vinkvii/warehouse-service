package com.warehouse.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.channel.PublishSubscribeChannel;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.context.IntegrationFlowContext;
import org.springframework.integration.ip.dsl.Udp;

import java.util.Map;
import java.util.concurrent.CountDownLatch;

@Configuration
@Getter
public class UdpConfig {

    @Value("#{${spring.application.udp.channels}}")
    private Map<String, Integer> channelsMap;

    @Value("${spring.application.countDownLatchValue:0}")
    private int countDownLatchValue;

    @Autowired
    private IntegrationFlowContext flowContext;

    public CountDownLatch latch;

    @Bean
    public PublishSubscribeChannel temperatureChannel() {
        return new PublishSubscribeChannel();
    }

    @Bean
    public PublishSubscribeChannel humidityChannel() {
        return new PublishSubscribeChannel();
    }

    @Bean
    public ApplicationRunner runner() {
        return args -> {
            for (Map.Entry<String, Integer> channel : channelsMap.entrySet()) {
                makeANewUdpInbound(channel.getKey(), channel.getValue());
            }
            if (countDownLatchValue > 0) {
                latch = new CountDownLatch(countDownLatchValue);
                try {
                    // Keep the application alive
                    latch.await();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        };
    }

    public void makeANewUdpInbound(String channel, Integer port) {
        //System.out.println("Creating an adapter to receive from port " + port);
        IntegrationFlow flow = IntegrationFlow.from(Udp.inboundAdapter(port))
                .<byte[], String>transform(String::new).channel(channel)
                .get();
        flowContext.registration(flow).register();
    }
}
