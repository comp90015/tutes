package stubs;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface Worker extends Remote {

  <T> T work(Task<T> t) throws RemoteException;

}
