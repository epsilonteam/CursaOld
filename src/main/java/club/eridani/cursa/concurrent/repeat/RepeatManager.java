package club.eridani.cursa.concurrent.repeat;

import club.eridani.cursa.concurrent.Task;
import club.eridani.cursa.concurrent.tasks.ObjectTask;

import java.util.ArrayList;
import java.util.List;
import java.util.function.IntSupplier;

import static club.eridani.cursa.concurrent.TaskManager.launch;

/**
 * Created by B_312 on 05/01/2021
 */
public class RepeatManager {

    public final List<RepeatUnit> repeatUnits = new ArrayList<>();
    RepeatThread repeatThread = new RepeatThread();

    RepeatManager() {
        repeatThread.start();
    }

    public static void runDelay() {
        synchronized (getInstance().repeatUnits) {
            getInstance().repeatUnits.removeIf(RepeatUnit::isDead);
            getInstance().repeatUnits.forEach(it -> {
                if (it.shouldRun()) {
                    launch(task -> it.run());
                }
            });
        }
    }

    //---- Delay Runner ----//
    public static <T> void addDelayTask(int delay, T parameter, Task<T> task) {
        runRepeat(delay, 1, true, repeat -> launch(parameter, task));
    }

    public static void addDelayTask(int delay, ObjectTask task) {
        runRepeat(delay, 1, true, repeat -> launch(task));
    }

    //---- Repeat Runner ----//
    public static void runRepeat(RepeatUnit unit) {
        registerRepeatUnit(unit);
    }

    public static void runRepeat(int delay, ObjectTask task) {
        RepeatUnit unit = new RepeatUnit(delay, task);
        registerRepeatUnit(unit);
    }

    public static void runRepeat(int delay, int times, ObjectTask task) {
        RepeatUnit unit = new RepeatUnit(delay, times, task);
        registerRepeatUnit(unit);
    }

    public static void runRepeat(int delay, int times, boolean isDelayTask, ObjectTask task) {
        RepeatUnit unit = new RepeatUnit(delay, times, isDelayTask, task);
        registerRepeatUnit(unit);
    }

    public static void runRepeat(IntSupplier delay, ObjectTask task) {
        RepeatUnit unit = new RepeatUnit(delay, task);
        registerRepeatUnit(unit);
    }

    public static void runRepeat(IntSupplier delay, int times, ObjectTask task) {
        RepeatUnit unit = new RepeatUnit(delay, times, task);
        registerRepeatUnit(unit);
    }

    public static void registerRepeatUnit(RepeatUnit repeatUnit) {
        synchronized (getInstance().repeatUnits) {
            getInstance().repeatUnits.add(repeatUnit);
        }
        if (getInstance().repeatThread == null) {
            getInstance().repeatThread = new RepeatThread();
            getInstance().repeatThread.start();
        }
    }

    public static void unregisterRepeatUnit(RepeatUnit repeatUnit) {
        synchronized (getInstance().repeatUnits) {
            getInstance().repeatUnits.remove(repeatUnit);
        }
    }

    //---- Instance Stuff ----//
    private static RepeatManager instance;

    public static RepeatManager getInstance() {
        if (instance == null) init();
        return instance;
    }

    public static void init() {
        instance = new RepeatManager();
    }

}
