import com.fasterxml.jackson.databind.ObjectMapper;
import com.kvoli.base.Command;
import com.kvoli.base.Packets;
import java.io.IOException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class PacketsTests {

  @Test
  public void shouldDe() throws IOException {
    String json = "{\"type\":\"identitychange\",\"identity\":\"james-bond007\"}";
    Packets.IdentityChange cmd = new ObjectMapper().readerFor(Packets.ToServer.class).readValue(json);
    Assertions.assertEquals("james-bond007", cmd.identity);
  }

  @Test
  public void shouldSer() throws IOException {
    String expected = "{\"type\":\"identitychange\",\"identity\":\"james-bond007\"}";
    Packets.IdentityChange identityChange = new Packets.IdentityChange();
    identityChange.identity = "james-bond007";

    String actual = new ObjectMapper().writeValueAsString(identityChange);
    Assertions.assertEquals(expected, actual);

  }
}
