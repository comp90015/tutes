/*
 Example Adapted from the great rox, see  niotut
 http://rox-xmlrpc.sourceforge.net/niotut/
 */
import java.nio.channels.SocketChannel;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class EchoWorker implements Runnable {
  private BlockingQueue<ServerDataEvent> queue = new LinkedBlockingQueue<>();

  public void processData(NioServer server, SocketChannel socket, byte[] data, int count) {
    byte[] dataCopy = new byte[count];
    System.arraycopy(data, 0, dataCopy, 0, count);
    queue.offer(new ServerDataEvent(server, socket, dataCopy));
  }

  public void run() {
    ServerDataEvent dataEvent;

    while(true) {
      // Wait for data to become available
      synchronized (queue) {
        try {
          dataEvent = queue.take();
        } catch (InterruptedException e) {
          continue;
        }
      }
      // Return to sender
      dataEvent.server.send(dataEvent.socket, dataEvent.data);
    }
  }
}
