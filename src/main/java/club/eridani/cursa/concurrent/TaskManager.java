package club.eridani.cursa.concurrent;

import club.eridani.cursa.concurrent.repeat.DelayUnit;
import club.eridani.cursa.concurrent.repeat.RepeatManager;
import club.eridani.cursa.concurrent.repeat.RepeatUnit;
import club.eridani.cursa.concurrent.task.*;
import club.eridani.cursa.concurrent.thread.BackgroundMainThread;
import club.eridani.cursa.concurrent.thread.BlockingContent;
import club.eridani.cursa.concurrent.utils.Syncer;
import club.eridani.cursa.tasks.Tasks;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.*;
import java.util.function.IntSupplier;

/**
 * Created by B_312 on 05/01/2021
 */
public class TaskManager {

    //---- Instance Stuff ----//
    public static TaskManager instance = new TaskManager();

    public static Logger logger = LogManager.getLogger("Concurrent Task Manager");

    public static int workingThreads = Runtime.getRuntime().availableProcessors();

    public BackgroundMainThread backgroundMainThread = new BackgroundMainThread();

    public final ThreadPoolExecutor executor = new ThreadPoolExecutor(workingThreads, Integer.MAX_VALUE, 1000, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<>());

    TaskManager() {
        RepeatManager.init();
        backgroundMainThread.start();
    }

    public static long runTiming(VoidTask task) {
        long startTime = System.currentTimeMillis();
        task.invoke();
        return System.currentTimeMillis() - startTime;
    }

    //---- TaskPool Runner ----//
    public static void runBlocking(BlockingTask task) {
        BlockingContent content = new BlockingContent();
        task.invoke(content);
        content.await();
    }

    public static void runBlockingTasks(VoidTask... tasks) {
        runBlockingTasks(Arrays.asList(tasks));
    }

    public static void runBlockingTasks(List<VoidTask> tasks) {
        Syncer syncer = new Syncer(tasks.size());
        tasks.forEach(it -> instance.executor.execute(new VoidRunnable(it, syncer)));
        syncer.await();
    }

    public static <T> void runParameterBlocking(List<MultiParameterTask<T>> tasks, T[] parameters) {
        Syncer syncer = new Syncer(tasks.size());
        tasks.forEach(it -> instance.executor.execute(new TaskRunnable<>(it, syncer, parameters)));
        syncer.await();
    }

    public static void launch(Tasks tasks) {
        instance.executor.execute(new VoidRunnable(tasks.task));
    }

    public static void launch(VoidTask task) {
        instance.executor.execute(new VoidRunnable(task));
    }

    public static <T> void launch(Syncer syncer, T parameters, EventTask<T> task) {
        instance.executor.execute(new TaskRunnable<>(task, syncer, parameters));
    }

    public static <T> void launch(T parameter, EventTask<T> task) {
        instance.executor.execute(new TaskRunnable<>(parameter, task));
    }

    public static <T> void launch(Syncer syncer, T[] parameters, MultiParameterTask<T> task) {
        instance.executor.execute(new TaskRunnable<>(task, syncer, parameters));
    }

    public static <T> void launch(T[] parameters, MultiParameterTask<T> task) {
        instance.executor.execute(new TaskRunnable<>(parameters, task));
    }

    public static void launch(Syncer syncer, VoidTask task) {
        instance.executor.execute(new VoidRunnable(task, syncer));
    }

    //---- Delay Runner ----//
    public static void addDelayTask(int delay, VoidTask task) {
        RepeatManager.instance.delayUnits.add(new DelayUnit(task, System.currentTimeMillis() + delay));
    }

    public static void addDelayTask(DelayUnit delayUnit) {
        RepeatManager.instance.delayUnits.add(delayUnit);
    }

    //---- Repeat Runner ----//
    public static void runRepeat(RepeatUnit unit) {
        registerRepeatUnit(unit);
    }

    public static void runRepeat(int delay, VoidTask task) {
        RepeatUnit unit = new RepeatUnit(delay, task);
        registerRepeatUnit(unit);
    }

    public static void runRepeat(int delay, int times, VoidTask task) {
        RepeatUnit unit = new RepeatUnit(delay, times, task);
        registerRepeatUnit(unit);
    }

    public static void runRepeat(IntSupplier delay, VoidTask task) {
        RepeatUnit unit = new RepeatUnit(delay, task);
        registerRepeatUnit(unit);
    }

    public static void runRepeat(IntSupplier delay, int times, VoidTask task) {
        RepeatUnit unit = new RepeatUnit(delay, times, task);
        registerRepeatUnit(unit);
    }

    public static void registerRepeatUnit(RepeatUnit repeatUnit) {
        RepeatManager.instance.repeatUnits.add(repeatUnit);
    }

    public static void unregisterRepeatUnit(RepeatUnit repeatUnit) {
        RepeatManager.instance.repeatUnits.remove(repeatUnit);
    }

    public static void repeat(int times, VoidTask task) {
        for (int i = 0; i < times; i++) {
            task.invoke();
        }
    }

    public void stop() {
        try {
            executor.shutdown();
        } catch (Exception ignore) {
            logger.info("TaskManager shut down!");
        }
    }

    //---- Background Stuff ----//
    public static void updateBackground() {
        RepeatManager.update();
    }

}
