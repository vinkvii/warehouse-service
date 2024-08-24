package com.warehouse;

import com.warehouse.service.Impl.EventServiceImpl;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.verify;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
class WarehouseServiceApplicationTests {
    @MockBean
    EventServiceImpl eventService;

    @BeforeEach
    void beforeEach() throws ExecutionException, InterruptedException {
        CompletableFuture temperatureFuture = CompletableFuture.runAsync(() -> {
            TemperatureSensor.start();
        });
        CompletableFuture humidityFuture = CompletableFuture.runAsync(() -> {
            HumiditySensor.start();
        });
        CompletableFuture.allOf(temperatureFuture, humidityFuture).get();
    }

    @AfterEach
    void afterEach() {
        TemperatureSensor.stop();
        HumiditySensor.stop();
    }

    @Test
    void contextLoads() throws InterruptedException {
        Thread.sleep(2000);
        verify(eventService, atLeast(1)).sendEvent(anyString());
    }

}
