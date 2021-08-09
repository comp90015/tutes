# COMP90015 Challenge 3

> Implement a fixed size thread pool that accepts tasks implementing the Runnable interface and schedules task execution on threads.

The main goal here is to implement the executor and the run methods inside the ThreadPool.

Unlike the previous week where we spawned a thread for each new tcp socket connection. Here we want to have a fixed number of threads, which
we schedule tasks over.

The methods that may come in handy here are:

```java
object.wait() // <--- this thread will wait until someone calls object.notify()
object.notify() // <- "awaken" a thread to start executing
```

We ideally want to avoid any race conditions, as such we may wish to synchronize access to our queue datastructure

```java
synchronized (taskQueue) {
    // do stuff in the critical section, with taskQueue
}
```
