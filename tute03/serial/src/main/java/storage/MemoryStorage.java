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

package storage;

import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class MemoryStorage implements KV{
  private HashMap<ByteBuffer, byte[]> store = new HashMap<>();
  private ReentrantReadWriteLock storeLock = new ReentrantReadWriteLock();

  @Override
  public byte[] get(byte[] key) {
    byte[] value;
    storeLock.readLock().lock();
    try {
      value = store.get(ByteBuffer.wrap(key));
    } finally {
      storeLock.readLock().unlock();
    }
    return value;
  }

  @Override
  public void set(byte[] key, byte[] value) {
    storeLock.writeLock().lock();
    try {
      store.put(ByteBuffer.wrap(key), value);
    } finally {
      storeLock.writeLock().unlock();
    }
  }

  @Override
  public int del(byte[] key) {
    byte[] removed;
    storeLock.writeLock().lock();
    try {
      removed = store.remove(ByteBuffer.wrap(key));
    } finally {
      storeLock.writeLock().unlock();
    }
    return removed == null ? 0 : 1;
  }
}
