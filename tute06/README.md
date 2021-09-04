# COMP90015 Challenge 6

### Todo

**required**

Correct the issues in the Client/Server so that it runs correctly

```
rmi/src/main/java/client/Client
rmi/src/main/java/server/ServerWorker
```

**optional (recommended for practice)**

Implement a multi-server backend for the redis kv-store from week 4

It should support approx. the following iface:

```java
public interface ClusterServer extends Remote {

    /* request that this remote server apply the operations within cluster request
       You get to decide what is in req/response, however it needs to support get/set/del
    */
    ClusterResponse apply(ClusterRequest req) throws RemoteException;

    /* A heartbeat msg to check liveness */
    int ping(int pong) throws RemoteException;

    /* request this remote servers peer list */
    List<ClusterServer> getPeers() throws RemoteException;

    /* register this server with the remote server, returns their updated server list */
    List<ClusterServer> register(ClusterServer self) throws RemoteException;
}
```

### Context

Java Remote Method Invocation (RMI) is a naitve Java mechanism to create distributed objects.

RMI abstracts away sockets, serialization and deseriaizlation when dealing with distributed applications.

Pros

- Many applications become simpler and quicker to build
- Less boilerplate code

Cons

- Java locked, other RPC protocols such as Thrift and gRPC are language agnostic.
- Not scalable to large workflows, better suited to education and smaller projects.
- Not commonly used.
- Performance.

The basic structure of RMI is as follows:

1. Declare the methods you wish to be exported by the "Server"

```java

public interface RemoteServer extends Remote {
    void remoteMethod() throws RemoteException;

    // type parameters are allowed
    <T> T work(Task<T> t) throws RemoteException;
}
```

2. Implement the concrete classes, for RemoteServer


```java

public class RServer implements RemoteServer {

    public static void main(String args[]) {
      // create registry to then register our object against
      Registry registry = LocateRegistry.createRegistry(1099);

      // create an instance of this class
      RServer rs = new RServer();

      // register the instance 
      RemoteServer stub = (RemoteServer) UnicastRemoteObject.exportObject(rs, 0);
      
      // now good to go!
    }

    @Override
    void remoteMethod() throws RemoteException {
        // do secret stuff
    }
}
```

3. Create a client to use the remote methods

```java
public class Client {

    public static void main(String args[]) {
        Registry registry = LocateRegistry.findRegistry(1099); // <- find the registry which has an index of objects
        RemoteServer rs = (RemoteServer) registry.lookup("RemoteServer"); // <- find the object
        rs.remoteMethod(); // <--- call the remote method defined in the iface
    }
}
```





