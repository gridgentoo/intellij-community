import java.util.concurrent.atomic.AtomicInteger;

// "Convert to atomic" "true"
class Test {
    final AtomicInteger o = new AtomicInteger(0);
  int j = o.get();

  void foo() {
    while ((o.set(j)) != 0) {}
  }
}