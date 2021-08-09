


// public static void main() {} <--- runs inside the main thread


// creating a static thread pool, which executes tasks implementing the Runnable interface

public class Main {
  private static final int NUM_THREADS = 6;

  public static void main(String[] args) {
    // n is the + add to tune the difficulty
    poolExec(20);
  }

  private static void singleExec(int n) {
    for (int i = 0; i < 20; i++) {
      FibTask newTask = new FibTask(i + n);
      newTask.run();
    }
  }

  private static void poolExec(int n) {
    ThreadPool threadPool = new ThreadPool(NUM_THREADS);
    for (int i = 0; i < 20; i++) {
      FibTask newTask = new FibTask(i + n);
      threadPool.execute(newTask);
    }
  }
}
