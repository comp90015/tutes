import com.fasterxml.jackson.databind.ObjectMapper;
import com.kvoli.base.Command;
import java.io.IOException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class CommandTest {

  @Test
  public void shouldDeserializeToMap() throws IOException {
    String json = "{\"type\":\"identitychange\",\"identity\":\"james-bond007\"}";
    Command cmd = new ObjectMapper().readerFor(Command.class).readValue(json);
    Assertions.assertEquals("identitychange", cmd.type);
    Assertions.assertEquals("james-bond007", cmd.get().get("identity"));
  }

  @Test
  public void shouldSerializeMapped() throws IOException {
    String expected = "{\"type\":\"identitychange\",\"identity\":\"james-bond007\"}";
    Command cmd = new Command();
    cmd.add("identity", "james-bond007");
    cmd.type = "identitychange";

    String actual = new ObjectMapper().writeValueAsString(cmd);
    Assertions.assertEquals(expected, actual);

  }
}
