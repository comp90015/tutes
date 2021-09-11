# COMP90015 Challenge 7

In this challenge we will again split it into an extension and core part.


## Core

### Context

So far we have been working with blocking IO. Where sockets had input and output streams. On these streams, we would call `is.read()` (or `os.write()`, however that didn't really block for long) which would block until there was something available to be read. This meant that the thread currently executing this code would be stuck on this line, until something was available to be read. We got around this limitation by introducing multiple threads, one per connection.

What we will be looking at now, is how we can use only a single thread for listening, accepting, writing and reading for N (`N >= 0`) connections. The way we can achieve this is with asynchronous IO!

Java has supported async IO, since Java 1.4. Where we have previously been using the `io` package, we will now use the `nio` package, (new input output).

The task for today is simple, we are going back to week 1 and implement an echo server! Except this time it will be with significantly more loc.

Take a look inside `src/main/java`

```
> tree src/main/java
src/main/java
├── ChangeRequest.java
├── EchoWorker.java
├── NioServer.java
└── ServerDataEvent.java
```
The file which we will primarily dealing with is `NioServer.java`. 

The code which needs to be implemented is highlighted with // TODO implement me.

In order to tackle this challenge, feel free to look at the reference material by rox: [here](http://rox-xmlrpc.sourceforge.net/niotut/).

## Extension

Last week the extension looked at using RMI for a multi-server, multi-client enabled redis implementation.

This week we will rip out RMI and put in our own networking and transport protocol for enabling IPC between servers.

It is recommended to first tackle the above challenge before moving onto this.

You should re-use the iface from last week, modifying where necessary.

The basic principle is that you want to decorate / wrap each outgoing message with a context ID, such that any response you receive will contain the corresponding contextID.

In this fashion, you can now distinguish between messages you receive, which are responses to requests, and messages which are requests themselves or otherwise unrelated.

Additionally, if you really wish to spruce things up, add in 


```java
CompletableFuture<V>
```
For network timeout. Alternatively, you can tick your server with:

```java
Executors.newSingleThreadScheduledExecutor().schedule(() -> eventLoop(), 50, TimeUnit.MILLISECONDS);

public void eventLoop() { }
```

Which will tick the server at 20 tps (the same as minecraft and many other gaming servers).
