package club.eridani.cursa.concurrent.repeat;

import club.eridani.cursa.concurrent.task.VoidTask;
import club.eridani.cursa.concurrent.utils.Timer;

import java.util.function.IntSupplier;

/**
 * Created by B_312 on 05/01/2021
 */
public class RepeatUnit {

    VoidTask task;

    private volatile boolean isRunning = true;
    private volatile boolean isDead = false;
    public final IntSupplier delay;
    private final Timer timer = new Timer();
    private final VoidTask timeOutOperation;
    private int times = 0;
    private boolean isDelayTask = false;

    public RepeatUnit(int delay, VoidTask task) {
        this.task = task;
        this.delay = () -> delay;
        this.timeOutOperation = () -> {
        };
        this.times += Integer.MAX_VALUE;
    }

    public RepeatUnit(int delay, int times, VoidTask task) {
        this.task = task;
        this.delay = () -> delay;
        this.timeOutOperation = () -> {
        };
        this.times += times;
    }

    public RepeatUnit(int delay, int times, boolean isDelayTask, VoidTask task) {
        this.task = task;
        this.delay = () -> delay;
        this.timeOutOperation = () -> {
        };
        this.times += times;
        this.isDelayTask = isDelayTask;
    }

    public RepeatUnit(IntSupplier delay, VoidTask task) {
        this.task = task;
        this.delay = delay;
        this.timeOutOperation = () -> {
        };
        this.times += Integer.MAX_VALUE;
    }

    public RepeatUnit(IntSupplier delay, int times, VoidTask task) {
        this.task = task;
        this.delay = delay;
        this.timeOutOperation = () -> {
        };
        this.times += times;
    }

    public RepeatUnit(int delay, VoidTask timeOutOperation, VoidTask task) {
        this.task = task;
        this.delay = () -> delay;
        this.timeOutOperation = timeOutOperation;
        this.times += Integer.MAX_VALUE;
    }

    public RepeatUnit(int delay, int times, VoidTask timeOutOperation, VoidTask task) {
        this.task = task;
        this.delay = () -> delay;
        this.timeOutOperation = timeOutOperation;
        this.times += times;
    }

    public RepeatUnit(IntSupplier delay, int times, VoidTask timeOutOperation, VoidTask task) {
        this.task = task;
        this.delay = delay;
        this.timeOutOperation = timeOutOperation;
        this.times += times;
    }

    public void run() {
        if (isDelayTask) {
            isDelayTask = false;
            return;
        }
        if (isRunning && !isDead && task != null) {
            if (times > 0) {
                task.invoke();
                if (timer.passed(delay.getAsInt())) {
                    timeOutOperation.invoke();
                    suspend();
                }
                times--;
            } else {
                stop();
            }
        }
    }

    public boolean shouldRun() {
        if (timer.passed(delay.getAsInt())) {
            timer.reset();
            return true;
        } else return false;
    }

    public boolean isRunning() {
        return isRunning;
    }

    public void suspend() {
        isRunning = false;
    }

    public void resume() {
        isRunning = true;
    }

    public void stop() {
        isDead = true;
        task = null;
        isRunning = false;
    }

    public boolean isDead() {
        return isDead;
    }

}
