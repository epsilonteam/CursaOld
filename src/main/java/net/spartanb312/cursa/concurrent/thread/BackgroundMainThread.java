package net.spartanb312.cursa.concurrent.thread;

import net.spartanb312.cursa.concurrent.TaskManager;
import net.spartanb312.cursa.concurrent.utils.ThreadUtil;

/**
 * Created by B_312 on 05/01/2021
 */
public class BackgroundMainThread extends Thread {

    @Override
    public void run() {
        while (TaskManager.instance != null) {
            try {
                TaskManager.updateBackground();
            } catch (Exception exception) {
                TaskManager.logger.error("Running an unsafe task in background main thread!Please check twice!" +
                        "You'd better run an unsafe task by launching a new task or surrounding with try catch " +
                        "instead of running directly in background main thread!");
                exception.printStackTrace();
            }
            ThreadUtil.delay();
        }
    }

}
