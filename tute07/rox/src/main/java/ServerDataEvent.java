/*
 Example Adapted from the great rox, see  niotut
 http://rox-xmlrpc.sourceforge.net/niotut/
 */
import java.nio.channels.SocketChannel;

class ServerDataEvent {
  public NioServer server;
  public SocketChannel socket;
  public byte[] data;

  public ServerDataEvent(NioServer server, SocketChannel socket, byte[] data) {
    this.server = server;
    this.socket = socket;
    this.data = data;
  }
}
