package club.eridani.cursa.concurrent;

import club.eridani.cursa.concurrent.background.BackgroundTaskThread;

import java.util.List;
import java.util.Queue;
import java.util.concurrent.*;

/**
 * Created by B_312 on 05/01/2021
 */
public class TaskManager {

    public static int MAX_THREAD_COUNT = 8;

    public final BackgroundTaskThread backgroundThread = new BackgroundTaskThread();

    private final Queue<Task> backgroundTasks = new LinkedBlockingQueue<>();

    public final ThreadPoolExecutor executor = new ThreadPoolExecutor(MAX_THREAD_COUNT, Integer.MAX_VALUE, 1000, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<>());

    TaskManager() {
        backgroundThread.start();
    }

    public static void runBackground(Task task){
        getInstance().backgroundTasks.add(task);
        if(getInstance().backgroundThread.isSuspended) getInstance().backgroundThread.resumeThread();
    }

    public static void launch(Task task) {
        getInstance().executor.execute(new TaskRunnable(task));
    }

    public static void runBlocking(Task task) {
        CountDownLatch latch = new CountDownLatch(1);
        getInstance().executor.execute(new TaskRunnable(task,latch));
        try {
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void launch(CountDownLatch latch,Task task){
        getInstance().executor.execute(new TaskRunnable(task,latch));
    }

    public static void launch(Syncer syncer,Task task){
        getInstance().executor.execute(new TaskRunnable(task,syncer.getLatch()));
    }

    public static void runBlocking(List<Task> tasks) {
        CountDownLatch latch = new CountDownLatch(tasks.size());
        tasks.forEach(it -> getInstance().executor.execute(new TaskRunnable(it,latch)));
        try {
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public Task getBackgroundTask() {
        return backgroundTasks.poll();
    }

    private static TaskManager instance;

    public static TaskManager getInstance() {
        if (instance == null) init();
        return instance;
    }

    public static void init() {
        instance = new TaskManager();
    }

}
