# COMP90015 Challenge 4

> Implement the specified subset of the Redis Serialization Protocol (RESP), then using a multithreaded blocking tcp server - add the GET SET DEL commands

RESP has been praised for its simplicity that makes it much easier to get correct - than other protocols.

[Specification](https://redis.io/topics/protocol)

We will be working on this example for the coming weeks, building up a key-value store - the foundation of many distributed systems!

The file where there is unimplemented code is:

```
serial/src/main/java/proto/Protocol.java
```

### Testing

In order to test your code, you can use the redis-cli. If you can successfully perform get/set/del operations - then its working!

Make sure you read the redis specification to gain an understanding of how it works.

To put it simply, everything is a string terminated by `\r\n` or carriage return `0x0D` and line feed `0x0A`. When put together, these indicate the end of line `EOL` `0x0D 0xA`.

When reading from a stream, there is fundamentally only bytes until you de-serialize them into variables that make sense to your code.

Likewise, we can't just send an entire object over the wire (well you can have Java do it for you but then only a Java program can read it!). Instead, we must **serialize** our data back into `byte` format.

There are many well known serialization protocols such as protobufs or flatbuffers from google and facebook respectively. These are usually used in conjunction with an Remote Procedure Call (RPC) framework such as gRPC or Thrift.

Fortunately, as previously stated - RESP is an exceedingly pleasant protocol relative to its alternatives (see any game packet protocol for more).
