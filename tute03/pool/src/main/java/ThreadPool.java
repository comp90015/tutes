import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class ThreadPool {

  private final int nThreads;
  private final BlockingQueue<Runnable> taskQueue;
  private final List<ThreadWorker> threads;

  public ThreadPool(int nThreads) {
    this.nThreads = nThreads;
    this.taskQueue = new LinkedBlockingQueue<>();
    this.threads = new ArrayList<>();

    for (int i = 0; i < nThreads; i++) {
      threads.add(new ThreadWorker());
      threads.get(i).start();
    }
  }

  public void execute(Runnable task) {
    // TODO implement me :)
  }

  private class ThreadWorker extends Thread {

    @Override
    public void run() {
      // TODO implement me :)
    }
  }
}
