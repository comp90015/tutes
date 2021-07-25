# COMP90015 Challenge 1

>Implement a single threaded blocking server, that accepts client TCP connections on port 6379 and echoes all messages that it receives back on the socket.


You should use the two TCP socket classes provided by Java 11:

```java
java.net.ServerSocket;
java.net.Socket;
```

They can be used as follows


```java
ServerSocket serverSocket;
try {
    serverSocket = new ServerSocket();
    serverSocket.setSoTimeout(TIMEOUT * 1000);
    serverSocket.bind(new InetSocketAddress(host, port));
    while (true) {
        Socket conn = serverSocket.accept();
    }
} catch (IOException e) {
    // handle the exception
}
```

To test your server you can use telnet or netcat (or even write your own!)

I encourage you to see the documentation `man [posix tool here]`, but shortcuts are:

```bash
# netcat
netcat localhost 6379

# telnet (make sure you know how to exit)
telnet localhost 6379
```
