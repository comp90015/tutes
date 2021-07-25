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

import proto.replies.BulkReply;
import proto.replies.IntegerReply;
import proto.replies.StatusReply;

public interface KeeperServer {
  /**
   * Get the value of a key
   * String
   *
   * @param key0
   * @return BulkReply
   */
  BulkReply get(byte[] key0) throws KeeperException;

  /**
   * Set the string value of a key
   * String
   *
   * @param key0
   * @param value1
   * @return StatusReply
   */
  StatusReply set(byte[] key0, byte[] value1) throws KeeperException;

  /**
   * Delete a key
   * Generic
   *
   * @param key0
   * @return IntegerReply
   */
  IntegerReply del(byte[] key0) throws KeeperException;

  /**
   * Ping the server
   * Connection
   *
   * @return StatusReply
   */
  StatusReply ping() throws KeeperException;

  /**
   * Close the connection
   * Connection
   *
   * @return StatusReply
   */
  StatusReply quit() throws KeeperException;
}
