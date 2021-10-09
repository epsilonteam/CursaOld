package net.spartanb312.cursa.concurrent.thread;

import net.spartanb312.cursa.concurrent.TaskManager;
import net.spartanb312.cursa.concurrent.task.BlockingUnit;
import net.spartanb312.cursa.concurrent.task.VoidTask;
import net.spartanb312.cursa.concurrent.utils.Syncer;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class BlockingContent {

    private final List<BlockingUnit> tasks = new ArrayList<>();
    private final AtomicInteger finished = new AtomicInteger(0);
    private Syncer syncer;

    public void launch(VoidTask task) {
        BlockingUnit unit = new BlockingUnit(task, this);
        TaskManager.instance.executor.execute(unit);
        tasks.add(unit);
    }

    //Invoke this synchronized method to count.If the blocking thread invoked await(),then the child tasks will start counting down syncer
    public synchronized void count() {
        //Double lock to make sure we will wait for correct count
        synchronized (finished) {
            finished.incrementAndGet();
        }
    }

    public void await() {
        if (tasks.size() == 0) return;
        //Lock the count first to create a syncer.No need to lock tasks because calling this method at the end of runBlocking.No more tasks will be launch.
        synchronized (finished) {
            syncer = new Syncer(tasks.size() - finished.get());
        }
        syncer.await();
    }

    public synchronized void countDown() {
        if (syncer != null) syncer.countDown();
    }

}
