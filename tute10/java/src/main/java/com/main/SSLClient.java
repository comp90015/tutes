package com.main;

import java.io.*;
import javax.net.ssl.*;
import java.nio.*;

/**
 * Exercise 1 Client
 */
public class SSLClient {

    private static final String REMOTE_HOST = "localhost";
    private static final int REMOTE_PORT = 8282;
    private static final String TRUSTSTORE_LOCATION = SSLClient.class.getResource("/client/ClientTrustStore.jks").getPath();
    private static final String TRUSTSTORE_PASSWORD = "experttls";

    // Entry point
    public static void main(String[] args) {
        // You can also update properties as a command line parameter e.g.
        //     -Djavax.net.ssl.keyStore="keystore_location"
        System.setProperty("javax.net.ssl.trustStore", TRUSTSTORE_LOCATION);
        System.setProperty("javax.net.ssl.trustStorePassword", TRUSTSTORE_PASSWORD);

        // Part 1
        // Add code here to obtain a default socket factory, an SSL socket, and then make a connection

        // Obtain the default socket factory
        SSLSocketFactory f = null;

        try {
            // Create a socket - will not connect yet
            SSLSocket socket = null;

            // Handshake to create a session



            if (socket == null) {
                return;
            }

            // What parameters were established?
            System.out.println(String.format("Negotiated Session: %s", socket.getSession().getProtocol()));
            System.out.println(String.format("Cipher Suite: %s", socket.getSession().getCipherSuite()));

            // We're reading and writing bytes. Other streams can be used.
            BufferedOutputStream output = new BufferedOutputStream(socket.getOutputStream());
            BufferedInputStream input = new BufferedInputStream(socket.getInputStream());
            output.write(new byte[] { 2, 3, 5, 7, 11, 13, 17, 19, 23 });
            output.flush();

            // Read the server response up to a max of 64 bytes.
            byte[] serverResponse = new byte[64];
            int len = input.read(serverResponse, 0, 64);

            // Server response is an int, so should be 4 bytes
            if (len == 4) {
                ByteBuffer result = ByteBuffer.allocate(4).put(serverResponse, 0, 4);
                System.out.println("\nServer result: " + result.getInt(0));
            }

            input.close();
            output.close();
            socket.close();
        } catch (Exception e) {
            System.err.println("Exception: " + e.toString());
        }
    }
}