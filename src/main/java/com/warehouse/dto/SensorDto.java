package com.warehouse.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SensorDto {
    private String sensorId;
    private int value;
    private String sensorType;
}