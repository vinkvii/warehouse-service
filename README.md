---

# Warehouse Service

This project is a Spring Boot-based service designed to handle incoming UDP messages and process them using Kafka. The service listens on specified UDP ports, transforms the incoming data, and publishes the messages to Kafka topics for further processing.

## Table of Contents

- [Requirements](#requirements)
- [Project Structure](#project-structure)
- [Configuration](#configuration)
- [Setup](#setup)
- [Running the Service](#running-the-service)
- [Kafka Topics](#kafka-topics)
- [UDP Ports](#udp-ports)
- [Customization](#customization)

## Requirements

Before running the service, ensure you have the following installed:

- Java 11+
- Maven
- Docker (for running Kafka and Zookeeper)
- Kafka (if running locally without Docker)

## Project Structure

```
.
├── src
│   ├── main
│   │   ├── java
│   │   │   └── com.warehouse
│   │   │       ├── config
│   │   │       │   ├── UdpConfig.java
│   │   │       │   └── KafkaProducerConfig.java
│   │   │       ├── service
│   │   │       │   ├── UdpInboundMessageHandler.java
│   │   │       │   └── EventService.java
│   │   │       └── WarehouseServiceApplication.java
│   │   └── resources
│   │       ├── application.yml
│   │       
└── pom.xml
```

- **UdpConfig.java**: Configures the UDP listeners and integrates them with Spring Integration.
- **KafkaProducerConfig.java**: Configures Kafka prodcuer.
- **UdpInboundMessageHandler.java**: Handles the incoming UDP messages.
- **EventService.java**: Responsible for publishing messages to Kafka topics.
- **application.yml**: Configuration file for UDP channels and Kafka settings.

## Configuration

The `application.yml` file allows you to configure the UDP ports and Kafka settings. Here is a sample configuration:

```yaml
spring:
  kafka:
    producer:
      bootstrap-servers: 127.0.0.1:9092
      key-serializer: org.apache.kafka.common.serialization.StringSerializer
      value-serializer: org.apache.kafka.common.serialization.StringSerializer
      topic: sensor-topic

# UDP Channel Configuration
udp:
  channels: '{ "temperature": 4002,"humidity": 4003 }'
```

## Setup

### 1. Clone the repository:

```bash
git clone https://github.com/vinkvii/warehouse-service.git
cd warehouse-service
```

### 2. Build the project using Maven:

```bash
mvn clean install
```

### 3. Run Kafka and Zookeeper:

You can run Kafka and Zookeeper locally using Docker. Here's a simple Docker command to get Kafka and Zookeeper running:

```bash
docker-compose up
```

Or, you can run them locally without Docker by following the [Kafka Quickstart Guide](https://kafka.apache.org/quickstart).

### 4. Configure Kafka:

Ensure that the `application.yml` file has the correct Kafka bootstrap server address.

## Running the Service

After setting up the environment, you can run the service using:

```bash
mvn spring-boot:run
```

The service will start listening on the configured UDP ports (e.g., `4002` for temperature and `4003` for humidity) and will process the incoming messages.

## Kafka Topics

This service publishes the incoming sensor data to Kafka topics. You can customize the topics in the `application.yml` file.

Example:
```yaml
spring:
  kafka:
    producer:
      bootstrap-servers: 127.0.0.1:9092
      key-serializer: org.apache.kafka.common.serialization.StringSerializer
      value-serializer: org.apache.kafka.common.serialization.StringSerializer
      topic: sensor-topic
```

## UDP Ports

The service listens on the UDP ports defined in the `application.yml` file. The ports are associated with different sensor channels (e.g., temperature, humidity).

Example configuration:
```yaml
udp:
  channels: '{ "temperature": 4002,"humidity": 4003 }'
```

## Customization

You can customize the service by:

- **Modifying the UDP channels**: Add or remove sensor channels in the `application.yml` file.
- **Adding Kafka Topics**: Customize the Kafka topics for publishing sensor data.
- **Message Parsing**: Modify the message handlers in `UdpInboundMessageHandler.java` to handle different data formats.

---

This `README.md` provides the basic setup and instructions for running the `warehouse-service` that listens on UDP ports and publishes to Kafka. Let me know if you need any further adjustments!
