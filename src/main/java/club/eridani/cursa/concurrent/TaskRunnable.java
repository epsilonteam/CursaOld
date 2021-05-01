package club.eridani.cursa.concurrent;

import java.util.concurrent.CountDownLatch;

/**
 * Created by B_312 on 05/01/2021
 */
public class TaskRunnable implements Runnable {

    private final Task task;
    private final CountDownLatch latch;

    public TaskRunnable(Task task) {
        this.task = task;
        this.latch = null;
    }

    public TaskRunnable(Task task, CountDownLatch latch) {
        this.task = task;
        this.latch = latch;
    }

    @Override
    public void run() {
        task.invoke();
        if (latch != null) latch.countDown();
    }

}
