package client;

import java.io.Serializable;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import server.ServerWorker;
import stubs.Task;
import stubs.Worker;

/*
 TODO Find the mistakes in the code below!
 */
public class Client {
  public static void main(String[] args) {
    try {
      Registry registry = LocateRegistry.createRegistry(1099);
      ServerWorker worker  = (ServerWorker) registry.lookup("ServerWorker");

      Task<Integer> fibTask = (Task<Integer> & Serializable)() -> {
        int target = 20;
        int dp[] = new int[target + 1];
        dp[0] = dp[1] = 1;
        for (int i = 2; i < target; i++) {
          dp[i] = dp[i - 1] + dp[i - 2];
        }
        return dp[target-1];
      };

      Integer ret = worker.work(fibTask);

      System.out.println(ret);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
