package club.eridani.cursa.concurrent.task;

import java.util.concurrent.CountDownLatch;

/**
 * Created by B_312 on 05/01/2021
 */
public class VoidRunnable implements Runnable {

    private final VoidTask task;
    private final CountDownLatch latch;

    public VoidRunnable(VoidTask task) {
        this.task = task;
        this.latch = null;
    }

    public VoidRunnable(VoidTask task, CountDownLatch latch) {
        this.task = task;
        this.latch = latch;
    }

    @Override
    public void run() {
        task.invoke();
        if (latch != null) latch.countDown();
    }

}
