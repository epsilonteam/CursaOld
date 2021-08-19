package club.eridani.cursa.concurrent.task;

import club.eridani.cursa.concurrent.utils.Syncer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by B_312 on 05/01/2021
 */
public class TaskRunnable<T> extends TaskUnit implements Runnable {

    private final MultiParameterTask<T> task;
    private final List<T> parameters = new ArrayList<>();

    private EventTask<T> eventTask = null;
    private T eventParameter = null;

    public TaskRunnable(EventTask<T> eventTask) {
        this.eventTask = eventTask;
        this.task = null;
        this.syncer = null;
    }

    public TaskRunnable(EventTask<T> eventTask, Syncer syncer) {
        this.eventTask = eventTask;
        this.syncer = syncer;
        this.task = null;
        this.syncer = null;
    }

    public TaskRunnable(T eventParameter, EventTask<T> eventTask) {
        this.eventTask = eventTask;
        this.eventParameter = eventParameter;
        this.task = null;
        this.syncer = null;
    }

    public TaskRunnable(EventTask<T> eventTask, Syncer syncer, T eventParameter) {
        this.eventTask = eventTask;
        this.eventParameter = eventParameter;
        this.syncer = syncer;
        this.task = null;
        this.syncer = null;
    }

    public TaskRunnable(MultiParameterTask<T> task) {
        this.task = task;
        this.syncer = null;
    }

    public TaskRunnable(MultiParameterTask<T> task, Syncer syncer) {
        this.task = task;
        this.syncer = syncer;
    }

    public TaskRunnable(T parameter, MultiParameterTask<T> task) {
        this.task = task;
        this.syncer = null;
        this.parameters.add(parameter);
    }

    public TaskRunnable(MultiParameterTask<T> task, Syncer syncer, T parameter) {
        this.task = task;
        this.syncer = syncer;
        this.parameters.add(parameter);
    }

    public TaskRunnable(T[] parameters, MultiParameterTask<T> task) {
        this.task = task;
        this.syncer = null;
        this.parameters.addAll(Arrays.asList(parameters));
    }

    public TaskRunnable(MultiParameterTask<T> task, Syncer syncer, T[] parameters) {
        this.task = task;
        this.syncer = syncer;
        this.parameters.addAll(Arrays.asList(parameters));
    }

    @Override
    public void run() {
        try {
            if (eventTask != null) eventTask.invoke(eventParameter);
            else if (task != null) task.invoke(parameters);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        if (syncer != null) syncer.countDown();
    }

}
