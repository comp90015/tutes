public class FibTask implements Runnable {
  private int fibNumber;

  public FibTask(int fibNumber) {
    this.fibNumber = fibNumber;
  }

  @Override
  public void run() {
    int result = fib(fibNumber);
    System.out.printf("Result for %d is %d\n", fibNumber, result);
  }

  private int fib(int n) {
    if (n <= 1) return 1;
    if (n <= 2) return 2;
    return fib(n-1) + fib(n-2);
  }
}
