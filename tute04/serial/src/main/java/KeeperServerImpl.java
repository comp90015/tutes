import proto.KeeperException;
import proto.KeeperServer;
import proto.replies.BulkReply;
import proto.replies.IntegerReply;
import proto.replies.StatusReply;
import storage.KV;
import storage.MemoryStorage;

public class KeeperServerImpl implements KeeperServer {
  private final KV storage;

  public KeeperServerImpl() {
    this.storage = new MemoryStorage();
  }

  @Override
  public BulkReply get(byte[] key0) throws KeeperException {
    byte[] value = storage.get(key0);
    return new BulkReply(value);
  }

  @Override
  public StatusReply ping() throws KeeperException {
    return StatusReply.PONG;
  }

  @Override
  public StatusReply quit() throws KeeperException {
    return StatusReply.EXIT;
  }

  @Override
  public StatusReply set(byte[] key0, byte[] value1) throws KeeperException {
    storage.set(key0, value1);
    return new StatusReply("OK");
  }

  @Override
  public IntegerReply del(byte[] key0) throws KeeperException {
    return new IntegerReply(storage.del(key0));
  }
}
