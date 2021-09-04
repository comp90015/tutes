package server;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import stubs.Task;
import stubs.Worker;


/*
 TODO Find the mistakes in the code below!
 */
public class ServerWorker implements Worker {

  public static void main(String[] args) {
    try {
      Registry registry = LocateRegistry.getRegistry(1099);

      ServerWorker serverWorker = new ServerWorker();

      Worker stub = (Worker) UnicastRemoteObject.exportObject(serverWorker, 0);

      registry.rebind("Worker", stub);

    } catch (RemoteException e) {
      System.out.println("unable to register remote object");
      e.printStackTrace();
    }
  }

  @Override
  public <T> T work(Task<T> t) throws RemoteException {
    System.out.println("New task!");
    return t.execute();
  }
}
