package proto;

import static proto.Protocol.EMPTY_BYTES;

import java.nio.charset.StandardCharsets;

public class Command {
  private final Object name;
  private final Object[] objects;
  private final Object object1;
  private final Object object2;
  private final Object object3;

  public Command(Object name) {
    this(name, null, null, null, null);
  }

  public Command(Object name, Object[] objects, Object object1, Object object2,
                 Object object3) {
    this.name = name;
    this.objects = objects;
    this.object1 = object1;
    this.object2 = object2;
    this.object3 = object3;
  }

  public Command(Object[] objects) {
    this(null, objects, null, null, null);
  }

  public byte[] getName() {
    if (name != null) {
      return getBytes(name);
    }
    return getBytes(objects[0]);
  }

  public byte[] getBytes(Object object) {
    byte[] argument;
    if (object == null) {
      argument = EMPTY_BYTES;
    } else if (object instanceof byte[]) {
      argument = (byte[]) object;
    } else if (object instanceof String) {
      argument = ((String) object).getBytes(StandardCharsets.UTF_8);
    } else {
      argument = object.toString().getBytes(StandardCharsets.UTF_8);
    }
    return argument;
  }

  public void toArguments(Object[] arguments, Class<?>[] types) {
    int position = 0;
    for (Class<?> type : types) {
      if (type == byte[].class) {
        if (position >= arguments.length) {
          throw new IllegalArgumentException(
              "wrong number of arguments for '" + getName() + "' command");
        }
        if (objects.length - 1 > position) {
          arguments[position] = objects[1 + position];
        }
      } else {
        int left = objects.length - position - 1;
        byte[][] lastArgument = new byte[left][];
        for (int i = 0; i < left; i++) {
          lastArgument[i] = (byte[]) objects[i + position + 1];
        }
        arguments[position] = lastArgument;
      }
      position++;
    }
  }
}