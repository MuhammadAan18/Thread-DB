package concurrency;

import java.util.concurrent.*;
import java.util.function.IntConsumer;

public class TimerMeja {
  private final ScheduledExecutorService ses = Executors.newSingleThreadScheduledExecutor();
  private ScheduledFuture<?> task;

  public void start(int remainingSec, IntConsumer onTick, Runnable onDone) {
    stop(); 
    final long[] sisa = {remainingSec};
    task = ses.scheduleAtFixedRate(() -> {
      int now = (int) sisa[0];
      onTick.accept(Math.max(now, 0));
      if (now <= 0) { stop(); onDone.run(); }
      sisa[0] = now - 1;
    }, 0, 1, TimeUnit.SECONDS);
  }

  public void stop() {
    if (task != null) { task.cancel(true); task = null; }
  }

  public void shutdown() { ses.shutdownNow(); }
}
