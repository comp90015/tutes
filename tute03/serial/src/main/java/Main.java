/*
 * Copyright (c)  2021, kvoli
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

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Main {
  public static final int port = 6379;

  public static final Logger LOGGER = Logger.getLogger(Main.class.getName());

  public static void main(String[] args) {
    (new PoolServer()).run();
  }

  static class PoolServer {
    private NetworkHandler handler = new ClientHandler();
    private boolean alive;
    private ServerSocket serverSocket;

    public void close() {
      try {
        if (serverSocket != null && !serverSocket.isClosed()) {
          serverSocket.close();
        }
      } catch (IOException e) {
        LOGGER.log(Level.SEVERE, "Unable to close socket on port " + port, e);
      }
    }

    public void run() {
      try {
        serverSocket = new ServerSocket(port);
        LOGGER.info("Listening on port " + port);
        alive = true;
        while (alive) {
          Socket conn = serverSocket.accept();
          if (conn != null) {
            LOGGER.info("New connection received: " + conn.getRemoteSocketAddress().toString());
            handler.handle(conn);
          }
        }
      } catch (IOException e) {
        LOGGER.log(Level.WARNING, "Error handling connections ", e);
      } finally {
        close();
      }
    }
  }

}
