package net.spartanb312.cursa;

import net.spartanb312.cursa.concurrent.event.EventManager;
import net.spartanb312.cursa.concurrent.event.Listener;
import net.spartanb312.cursa.concurrent.event.Priority;
import net.spartanb312.cursa.event.events.client.InitializationEvent;
import net.spartanb312.cursa.module.modules.client.ClickGUI;
import net.spartanb312.cursa.client.*;
import net.spartanb312.cursa.concurrent.TaskManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.opengl.Display;

import static net.spartanb312.cursa.concurrent.TaskManager.*;

/**
 * Author B_312
 * Since 05/01/2021
 * Last update on 09/21/2021
 */
public class Cursa {

    public static final String MOD_NAME = "Cursa";
    public static final String MOD_VERSION = "b6";

    public static final String AUTHOR = "B_312";
    public static final String GITHUB = "https://github.com/SpartanB312/Cursa";

    public static String CHAT_SUFFIX = "\u1d04\u1d1c\u0280\u0073\u1d00";

    public static final Logger log = LogManager.getLogger(MOD_NAME);
    private static Thread mainThread;

    @Listener(priority = Priority.HIGHEST)
    public void preInitialize(InitializationEvent.PreInitialize event) {
        mainThread = Thread.currentThread();
    }

    @Listener(priority = Priority.HIGHEST)
    public void initialize(InitializationEvent.Initialize event) {
        long tookTime = TaskManager.runTiming(() -> {
            Display.setTitle(MOD_NAME + " " + MOD_VERSION);

            //Parallel load managers
            TaskManager.runBlocking(it -> {

                Cursa.log.info("Loading Font Manager");
                FontManager.init();

                Cursa.log.info("Loading Module Manager");
                ModuleManager.init();

                Cursa.log.info("Loading GUI Manager");
                launch(it, GUIManager::init);

                Cursa.log.info("Loading Command Manager");
                launch(it, CommandManager::init);

                Cursa.log.info("Loading Friend Manager");
                launch(it, FriendManager::init);

                Cursa.log.info("Loading Config Manager");
                launch(it, ConfigManager::init);

            });
        });

        log.info("Took " + tookTime + "ms to launch Cursa!");
    }

    @Listener(priority = Priority.HIGHEST)
    public void postInitialize(InitializationEvent.PostInitialize event) {
        ClickGUI.instance.disable();
    }

    public static boolean isMainThread(Thread thread) {
        return thread == mainThread;
    }

    public static EventManager EVENT_BUS = new EventManager();
    public static ModuleBus MODULE_BUS = new ModuleBus();

    public static final Cursa instance = new Cursa();

}