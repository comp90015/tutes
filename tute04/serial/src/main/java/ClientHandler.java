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

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.logging.Level;
import java.util.logging.Logger;
import proto.Command;
import proto.KeeperException;

public class ClientHandler implements NetworkHandler {
  public static final Logger LOGGER = Logger.getLogger(ClientHandler.class.getName());

  public static final int THREAD_POOL_SIZE = 7;
  private final ThreadPoolExecutor executor;
  private final CommandHandler commandHandler;
  private final CommandDecoder commandDecoder;

  public ClientHandler() {
    this.executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(THREAD_POOL_SIZE);
    this.commandHandler = new CommandHandler(new KeeperServerImpl());
    this.commandDecoder = new CommandDecoder();
  }

  @Override
  public void handle(Socket socket) {
    this.executor.execute(new ConnHandler(socket));
  }

  private class ConnHandler implements Runnable, ConnHandlerContext {
    private final Socket clientConn;
    private boolean alive;
    private BufferedInputStream is;
    private BufferedOutputStream os;

    public ConnHandler(Socket clientConn) {
      this.clientConn = clientConn;
      this.alive = false;
    }

    @Override
    public synchronized void open() {
      try {
        is = new BufferedInputStream(clientConn.getInputStream());
        os = new BufferedOutputStream(clientConn.getOutputStream());
      } catch (IOException e) {
        alive = false;
        LOGGER.log(Level.SEVERE, "Unable to open conn socket stream", e);
        e.printStackTrace();
      }
    }

    @Override
    public synchronized void close() {
      this.alive = false;
      try {
        if (!clientConn.isClosed()) {
          is.close();
          os.close();
          clientConn.close();
        }
      } catch (IOException e) {
        LOGGER.log(Level.SEVERE, "Unable to close conn socket stream", e);
        e.printStackTrace();
      }
    }

    @Override
    public void run() {
      Command cmd;
      alive = true;
      LOGGER.log(Level.INFO, "Handling client connection");

      // get io or die
      open();
      while (alive) {
        try {
          _run();
        } catch (Exception e) {
          close();
          LOGGER.log(Level.SEVERE, "Unrecoverable exception", e);
          e.printStackTrace();
        }
      }
      close();
    }

    private synchronized void _run() throws Exception {
      Command cmd;
      try {
        cmd = commandDecoder.decode(is);
        if (cmd == null) {
          close();
        } else {
          commandHandler.handle(this, os, cmd);
        }
      } catch (KeeperException e) {
        LOGGER.log(Level.WARNING, "Recoverable exception", e);
      }
    }
  }
}
