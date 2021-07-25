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

import java.io.BufferedOutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import proto.Command;
import proto.KeeperException;
import proto.KeeperServer;
import proto.replies.ErrorReply;
import proto.replies.Reply;
import proto.replies.StatusReply;

public class CommandHandler {
  private Map<ByteBuffer, Wrapper> methods = new HashMap<>();

  public CommandHandler(final KeeperServer server) {
    Class<? extends KeeperServer> serverImpl = server.getClass();
    for (final Method method : serverImpl.getMethods()) {
      final Class<?>[] types = method.getParameterTypes();
      methods.put(ByteBuffer.wrap(method.getName().getBytes(StandardCharsets.UTF_8)), cmd -> {
        Object[] objects = new Object[types.length];
        try {
          cmd.toArguments(objects, types);
          return (Reply) method.invoke(server, objects);
        } catch (IllegalAccessException e) {
          throw new KeeperException("Invalid argument type");
        } catch (InvocationTargetException e) {
          Throwable te = e.getTargetException();
          if (!(te instanceof KeeperException)) {
            te.printStackTrace();
          }
          return new ErrorReply("ERR " + te.getMessage());
        } catch (Exception e) {
          return new ErrorReply("ERR " + e.getMessage());
        }
      });
    }
  }

  public void handle(ConnHandlerContext ctx, BufferedOutputStream os, Command cmd) throws Exception {
    byte[] cmdName = cmd.getName();
    Wrapper wrapper = methods.get(ByteBuffer.wrap(cmdName));
    Reply reply;
    if (wrapper == null) {
      reply = new ErrorReply("Unknown command " + cmd.getName());
    } else {
      reply = wrapper.exec(cmd);
    }
    if (reply == StatusReply.EXIT) {
      ctx.close();
    }
    reply.write(os);
    os.flush();
  }

  interface Wrapper {
    Reply exec(Command cmd) throws KeeperException;
  }
}
