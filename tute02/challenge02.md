### Challenge 2

> Implement a thread-per-connection blocking server, that accepts client TCP connections on port 6379 and maintains a chat room - broadcasting messages it receives to all other connected clients.

In java there are two ways to interact with Threads:

1. Implement the `Runnable` iface

```java
class RunMeInThread implements Runnable {
    public void run() {
        System.out.println("I'm in a thread");
    }
}

(new Thread(new RunMeInThread())).start();
```

2. Extend the `Thread` class


```java
public class RunMeInThread extends Thread {
    public void run() {
        System.out.println("I'm in a thread");
    }
}

(new RunMeInThread()).start();
```
If we have shared variables (for example the container which has all the clients), then we may end up with races without defined synchronization semantics.

Luckily for us, all primitives in Java (except Long and Double) are automatically atomic - meaning that if we write/read we can rest assured no one wrote/read the variable as we began our operation till when we finished.

You may also use these two other Java intrinsics:

```
volatile Object object; // Force reads to main memory
synchronized { // code in here } // ensures an exclusive lock on this section
```


When multiple threads access try to access the same variable, it become tricky to serialize their access in a way that ensure **liveness** and **safety**.

**safety:** something bad doesn't happen
**liveness** something good eventually happens

Just what is *good* and *bad* is dependent the desired outcome (what is correct)

To relate it to our chat server, we can consider that we might only want **to process 1 broadcast at time**.

In that case, **safety** would be that at most 1 thread is in the critical section (broadcast) at any time

Whilst **liveness** would be that a thread who is trying to broadcast, will eventually do so (enter the critical section).
