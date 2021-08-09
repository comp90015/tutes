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

package proto;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public final class Protocol {

  public static final byte SIMPLE_STRING_BYTE = '+';
  public static final byte ERROR_BYTE = '-';
  public static final byte INTEGER_BYTE = ':';
  public static final byte BULK_STRING_BYTE = '$';
  public static final byte ARRAY_BYTE = '*';
  public static final char CR = '\r';
  public static final char LF = '\n';
  public static final byte[] NULL_BULK_STRING = {'-', '1', CR, LF};
  public static final byte NULL_BYTE = -1;
  public static final byte[] EMPTY_BYTES = new byte[0];

  // no init static class
  private Protocol() {
  }

  //
  public static byte[] readCrLfString(final BufferedInputStream is) throws IOException {

    return EMPTY_BYTES;
  }

  public static int readInteger(final BufferedInputStream is) throws IOException {
    // TODO implement me :)
    return NULL_BYTE;
  }

  public static byte[] readBulkString(final BufferedInputStream is) throws IOException {
    // TODO implement me :)
    return EMPTY_BYTES;
  }

  public static void readCrLf(final BufferedInputStream is) throws IOException {
    char next;
    next = (char) is.read();
    if (next != CR) {
      throw new IOException(String.format("Unexpected token in the input stream %c", next));
    }
    next = (char) is.read();
    if (next != LF) {
      throw new IOException(String.format("Unexpected token in the input stream %c", next));
    }
  }

  public static byte[][] readArray(final BufferedInputStream is) throws IOException {
    // TODO implement me :)
    return new byte[0][];
  }

  public static void writeCrLfString(final BufferedOutputStream os, byte[] data)
      throws IOException {
    writeCrLf(os);
  }

  public static void writeCrLfString(final BufferedOutputStream os, String data)
      throws IOException {
    writeCrLfString(os, data.getBytes(StandardCharsets.UTF_8));
  }

  public static void writeInteger(final BufferedOutputStream os, long data) throws IOException {
    writeCrLfString(os, Long.toString(data).getBytes(StandardCharsets.UTF_8));
  }

  public static void writeBulkString(final BufferedOutputStream os, byte[] data)
      throws IOException {
    // TODO implement me :)
  }

  public static void writeBulkString(final BufferedOutputStream os, String data)
      throws IOException {
    writeBulkString(os, data.getBytes(StandardCharsets.UTF_8));
  }

  private static void writeCrLf(final BufferedOutputStream os) throws IOException {
    os.write(CR);
    os.write(LF);
  }
}

