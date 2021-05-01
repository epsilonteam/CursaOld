package club.eridani.cursa.concurrent.background;

import club.eridani.cursa.concurrent.Task;
import club.eridani.cursa.concurrent.TaskManager;

/**
 * Created by B_312 on 05/01/2021
 */
public class BackgroundTaskThread extends Thread {

    public boolean shouldStop = false;
    public volatile boolean isSuspended = false;

    @Override
    public void run() {
        while (!shouldStop) {
            Task task = TaskManager.getInstance().getBackgroundTask();
            //Make sure no dead lock here
            if (task != null) {
                task.invoke();
            } else {
                isSuspended = true;
                this.suspend();
            }
        }
        this.interrupt();
    }

    public void resumeThread(){
        this.resume();
    }

}
