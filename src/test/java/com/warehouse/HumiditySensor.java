package com.warehouse;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;

public class HumiditySensor {

    static DatagramSocket socket = null;

    public static void start() {
       try {
                socket = new DatagramSocket();
                //InetAddress address = InetAddress.getByName("172.27.160.1");
                InetAddress address = InetAddress.getByName("127.0.0.1");
                int port = 4003;

                // Load the file from the resources folder
                Resource resource = new ClassPathResource("humidity_input.txt");
                List<String> messages = loadMessagesFromFile(resource);
                // Infinite loop to keep sending messages
                    // Iterate through each message in the file
                    for (String message : messages) {
                        byte[] buf = message.getBytes();
                        DatagramPacket packet = new DatagramPacket(buf, buf.length, address, port);

                        // Send the packet
                        socket.send(packet);
                        System.out.println("Sent Message: " + message + " to: " + address.getHostAddress());

                        // Sleep to simulate delay between messages
                        Thread.sleep(1000);
                    }
            } catch (SocketTimeoutException ex) {
                System.out.println("Timeout error: " + ex.getMessage());
                ex.printStackTrace();
            } catch (IOException ex) {
                System.out.println("Client error: " + ex.getMessage());
                ex.printStackTrace();
            } catch (InterruptedException ex) {
                Thread.currentThread().interrupt(); // Reset interrupted flag
                ex.printStackTrace();
            } finally {
                if (socket != null && !socket.isClosed()) {
                    socket.close();
                }
            }
    }

    public static void stop() {
        if (socket != null && !socket.isClosed()) {
            socket.close();
        }
    }

    private static List<String> loadMessagesFromFile(Resource resource) throws IOException {
        List<String> messages = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(resource.getInputStream()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                messages.add(line);
            }
        }

        return messages;
    }
}
