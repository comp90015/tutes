/*
 Example Adapted from the great rox, see  niotut
 http://rox-xmlrpc.sourceforge.net/niotut/
 */
import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.channels.spi.SelectorProvider;
import java.util.*;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;


public class NioServer implements Runnable {
  // The host:port combination to listen on
  private InetAddress hostAddress;
  private int port;


  // The channel on which we'll accept connections
  private ServerSocketChannel serverChannel;

  // The selector we'll be monitoring
  private Selector selector;

  // The buffer into which we'll read data when it's available
  private ByteBuffer readBuffer = ByteBuffer.allocate(8192);

  private EchoWorker worker;

  // A list of PendingChange instances
  private List pendingChanges = new LinkedList();

  // Maps a SocketChannel to a list of ByteBuffer instances
  private Map pendingData = new HashMap();

  public NioServer(InetAddress hostAddress, int port, EchoWorker worker) throws IOException {
    this.hostAddress = hostAddress;
    this.port = port;
    this.selector = this.initSelector();
    this.worker = worker;
  }

  public void send(SocketChannel socket, byte[] data) {
    synchronized (this.pendingChanges) {
      // Indicate we want the interest ops set changed
      this.pendingChanges.add(new ChangeRequest(socket, ChangeRequest.CHANGEOPS, SelectionKey.OP_WRITE));

      // And queue the data we want written
      synchronized (this.pendingData) {
        // TODO implement me
      }
    }

    // Finally, wake up our selecting thread so it can make the required changes
    this.selector.wakeup();
  }

  public void run() {
    Executors.newSingleThreadScheduledExecutor().schedule(() -> eventLoop(), 50, TimeUnit.MILLISECONDS);
    while (true) {
      try {
        // Process any pending changes
        synchronized (this.pendingChanges) {
          Iterator changes = this.pendingChanges.iterator();
          while (changes.hasNext()) {
            ChangeRequest change = (ChangeRequest) changes.next();
            switch (change.type) {
              case ChangeRequest.CHANGEOPS:
                SelectionKey key = change.socket.keyFor(this.selector);
                key.interestOps(change.ops);
            }
          }
          this.pendingChanges.clear();
        }

        // Wait for an event one of the registered channels
        this.selector.select();

        // Iterate over the set of keys for which events are available
        Iterator selectedKeys = this.selector.selectedKeys().iterator();
        while (selectedKeys.hasNext()) {
          SelectionKey key = (SelectionKey) selectedKeys.next();
          selectedKeys.remove();

          if (!key.isValid()) {
            continue;
          }

          // Check what event is available and deal with it
          if (key.isAcceptable()) {
            this.accept(key);
          } else if (key.isReadable()) {
            this.read(key);
          } else if (key.isWritable()) {
            this.write(key);
          }
        }
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
  }

  private void accept(SelectionKey key) throws IOException {
    // TODO implement me
  }

  private void read(SelectionKey key) throws IOException {
    // TODO implement me
  }

  private void write(SelectionKey key) throws IOException {
    // TODO implement me

  }

  private Selector initSelector() throws IOException {
    // Create a new selector
    Selector socketSelector = SelectorProvider.provider().openSelector();

    // Create a new non-blocking server socket channel
    this.serverChannel = ServerSocketChannel.open();
    serverChannel.configureBlocking(false);

    // Bind the server socket to the specified address and port
    InetSocketAddress isa = new InetSocketAddress(this.hostAddress, this.port);
    serverChannel.socket().bind(isa);

    // Register the server socket channel, indicating an interest in
    // accepting new connections
    serverChannel.register(socketSelector, SelectionKey.OP_ACCEPT);

    return socketSelector;
  }

  public static void main(String[] args) {
    try {
      EchoWorker worker = new EchoWorker();
      new Thread(worker).start();
      new Thread(new NioServer(null, 9090, worker)).start();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
