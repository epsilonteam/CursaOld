package club.eridani.cursa.concurrent;

import club.eridani.cursa.concurrent.background.BackgroundTaskThread;
import club.eridani.cursa.concurrent.repeat.RepeatManager;
import club.eridani.cursa.concurrent.tasks.ObjectTask;
import club.eridani.cursa.concurrent.utils.Syncer;

import java.util.List;
import java.util.Queue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by B_312 on 05/01/2021
 */
public class TaskManager {

    public static int MAX_THREAD_COUNT = 8;

    public final BackgroundTaskThread backgroundThread = new BackgroundTaskThread();

    private final Queue<Task<Object>> backgroundTasks = new LinkedBlockingQueue<>();

    public final ThreadPoolExecutor executor = new ThreadPoolExecutor(MAX_THREAD_COUNT, Integer.MAX_VALUE, 1000, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<>());

    TaskManager() {
        backgroundThread.start();
        RepeatManager.init();
    }

    //---- Background Runner ----//
    public static void runBackground(Task<Object> task) {
        getInstance().backgroundTasks.add(task);
        if (getInstance().backgroundThread.isSuspended) getInstance().backgroundThread.resumeThread();
    }

    public Task<Object> getBackgroundTask() {
        return backgroundTasks.poll();
    }

    //---- TaskPool Runner ----//
    public static void runBlocking(ObjectTask task) {
        CountDownLatch latch = new CountDownLatch(1);
        getInstance().executor.execute(new TaskRunnable<>(task, latch));
        try {
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void runBlocking(List<ObjectTask> tasks) {
        CountDownLatch latch = new CountDownLatch(tasks.size());
        tasks.forEach(it -> getInstance().executor.execute(new TaskRunnable<>(it, latch)));
        try {
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static <T> void runParameterBlocking(Task<T> task, T parameter) {
        CountDownLatch latch = new CountDownLatch(1);
        getInstance().executor.execute(new TaskRunnable<>(task, latch, parameter));
        try {
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static <T> void runParameterBlocking(List<Task<T>> tasks, T parameter) {
        CountDownLatch latch = new CountDownLatch(tasks.size());
        tasks.forEach(it -> getInstance().executor.execute(new TaskRunnable<>(it, latch, parameter)));
        try {
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void launch(ObjectTask task) {
        getInstance().executor.execute(new TaskRunnable<>(task));
    }

    public static <T> void launch(Syncer syncer, Task<T> task, T parameter) {
        getInstance().executor.execute(new TaskRunnable<>(task, syncer.getLatch(), parameter));
    }

    public static <T> void launch(T parameter, Task<T> task) {
        getInstance().executor.execute(new TaskRunnable<>(parameter, task));
    }

    public static void launch(CountDownLatch latch, ObjectTask task) {
        getInstance().executor.execute(new TaskRunnable<>(task, latch));
    }

    public static void launch(Syncer syncer, ObjectTask task) {
        getInstance().executor.execute(new TaskRunnable<>(task, syncer.getLatch()));
    }

    //---- Instance Stuff ----//
    private static TaskManager instance;

    public static TaskManager getInstance() {
        if (instance == null) init();
        return instance;
    }

    public static void init() {
        instance = new TaskManager();
    }

}
