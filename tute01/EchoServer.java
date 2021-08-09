/* Copyright (c)  2021, kvoli
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 *
 */

package skeletons;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Random;

/*
   Implement the TODO sections
   you can "echo" messages with the ridicule tO gEt tHe BeSt EfFecT
   */
public class EchoServer {
  public static final int port = 6379;
  private boolean handler_alive = false;

  public static void main(String[] args) {
    new EchoServer().handle();
  }

  public static String ridicule(String in) {
    Random random = new Random();
    StringBuilder sb = new StringBuilder();
    for (char c : in.toCharArray()) {
      sb.append(random.nextBoolean() ? c : Character.toUpperCase(c));
    }
    return sb.toString();
  }

  public void handle() {
    ServerSocket serverSocket;
    // we want to accept incoming tcp connections on port 6379
    // java has the SocketServer class that enables us to listen on this port
    // do the tcp dance and then offload a socket to us, which we then use to communicate
    // lets do that
    try {

      // lastly, lets log when we starting listening and when we recv a new connection.
      // That way we can easily see whats happening from the console.

      // we want to listen on port
      serverSocket = new ServerSocket(port);

      System.out.printf("Listening on port %d\n", port);
      handler_alive = true;

      // lets start accepting connections
      while (handler_alive) {
        // with the serverSocket.accept() method we can construct a new socket
        // this will only get called when a new connection is being established
        Socket newSocket = serverSocket.accept();
        // lets now be good programmers and encapsulate this into its own object
        EchoConnection conn = new EchoConnection(newSocket);

        if (conn != null) {
          // we have accepted a new connection, lets log it
          System.out.printf("Accepted new connection from %s:%d\n", newSocket.getLocalAddress().getCanonicalHostName(), newSocket.getPort());

          // finally, lets do some stuff with the socket inside the new object;
          conn.run();
        } else {
          handler_alive = false;
        }

      }
    } catch (IOException e) {
      // this also potentially throws an exception, so we will need to handle that (later)
      // if we get an IO exception here, there really is a big issue. We may not be able to recover
      // lets exit
      System.out.printf("Error handling conns, %s\n", e.getMessage());
      handler_alive = false;
    }
  }

  class EchoConnection {
    private Socket socket;
    private PrintWriter writer;
    private BufferedReader reader;
    private boolean connection_alive;

    public EchoConnection(Socket socket) throws IOException {
      // when we called the constructor, we created a reader and writer
      this.socket = socket;
      this.reader = new BufferedReader(new InputStreamReader(socket.getInputStream())); // we wrap these in some IO utility classes to make our life easier
      this.writer = new PrintWriter(socket.getOutputStream()); // likewise
    }

    public void close() {
      // we want to close all our streams and the socket itself
      // this could also be an IOException so lets wrap it
      try {
        reader.close();
        writer.close();
        socket.close();
      } catch (IOException e) {
        // we cant really do much here, lets just log the issue and continue
        System.out.println("Error closing conn");
      }
    }

    public void run() {
      connection_alive = true;
      while (connection_alive) {
        // now lets handle this connection
        // we will have it running in an infinite loop, that terminates when we set the flag connection_alive to false

        // same as before here, we could have the issue of IOExceptions as we read/write
        try {
          // lets read from the socket inputStream
          // luckily, we can read up until the new line char (\n) or \r\n

          // we might also read the null byte, (-1) in which case we should also kill this connection
          String inputLine = reader.readLine();
          if (inputLine == null) {
            // bad write, close
            connection_alive = false;
          } else {
            // good write, honor
            // we can log this to stdout
            System.out.printf("%d: %s\n", socket.getPort(), inputLine);
            // lets return this back, or "echo" what we receive
            writer.print(ridicule(inputLine));
            // lets ensure that we terminate it with a \n
            writer.println();
            // lets flush our buffer (just in case)
            writer.flush();
          }
        } catch (IOException e) {
          // if we get an IOException here, its probably best to kill the connection
          connection_alive = false;
        }
      }
      // lastly, lets handle closing the connection if we can... gracefully
      close();
    }
  }
}
