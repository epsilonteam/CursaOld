package club.eridani.cursa.concurrent.task;

import java.util.concurrent.CountDownLatch;

/**
 * Created by B_312 on 05/01/2021
 */
public class TaskRunnable<T> implements Runnable {

    private final Task<T> task;
    private final CountDownLatch latch;
    private final T parameter;

    public TaskRunnable(Task<T> task) {
        this.task = task;
        this.latch = null;
        this.parameter = null;
    }

    public TaskRunnable(Task<T> task, CountDownLatch latch) {
        this.task = task;
        this.latch = latch;
        this.parameter = null;
    }

    public TaskRunnable(T parameter, Task<T> task) {
        this.task = task;
        this.latch = null;
        this.parameter = parameter;
    }

    public TaskRunnable(Task<T> task, CountDownLatch latch, T parameter) {
        this.task = task;
        this.latch = latch;
        this.parameter = parameter;
    }

    @Override
    public void run() {
        task.invoke(parameter);
        if (latch != null) latch.countDown();
    }

}
