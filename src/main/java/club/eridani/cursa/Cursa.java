package club.eridani.cursa;

import club.eridani.cursa.client.*;
import club.eridani.cursa.concurrent.TaskManager;
import club.eridani.cursa.event.events.client.InitializationEvent;
import club.eridani.cursa.event.system.EventManager;
import club.eridani.cursa.event.system.Listener;
import club.eridani.cursa.event.system.impl.annotated.AnnotatedEventManager;
import club.eridani.cursa.tasks.Tasks;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.opengl.Display;

import static club.eridani.cursa.concurrent.TaskManager.*;
import static club.eridani.cursa.utils.ListUtil.listOf;

/**
 * The CursaMod's Client Base is under MIT License
 * But CursaAura is not included.CursaAura is under GNU Public License V3
 */
public class Cursa {

    public static final String MOD_NAME = "Cursa";
    public static final String MOD_VERSION = "b3";

    public static final String AUTHOR = "B_312";
    public static final String GITHUB = "https://github.com/SexyTeam/Cursa";

    public static String CHAT_SUFFIX = "\u1d04\u1d1c\u0280\u0073\u1d00";

    public static final Logger log = LogManager.getLogger(MOD_NAME);
    private static Thread mainThread;

    public Cursa() {
        instance = this;
        EVENT_BUS.register(this);
    }

    @Listener
    public void preInitialize(InitializationEvent.PreInitialize event) {
        mainThread = Thread.currentThread();
    }

    @Listener
    public void initialize(InitializationEvent.Initialize event) {

        long tookTime = runTiming(() -> {
            Display.setTitle(MOD_NAME + " " + MOD_VERSION);
            FontManager.init();
            log.info("Loading Module Manager");
            //ModuleManager is partial parallel loadable
            ModuleManager.init();

            //Parallel load managers
            runBlocking(it -> {
                it.launch(GUIManager::init);
                it.launch(CommandManager::init);
                it.launch(FriendManager::init);
                it.launch(ConfigManager::init);
            });
        });

        log.info("Took " + tookTime + "ms to launch Cursa!");
    }

    @Listener
    public void postInitialize(InitializationEvent.PostInitialize event) {
        launch(Tasks.LoadConfig);
    }

    public static boolean isMainThread(Thread thread) {
        return thread == mainThread;
    }

    public static EventManager EVENT_BUS = new AnnotatedEventManager();
    public static ModuleBus MODULE_BUS = new ModuleBus();

    private static Cursa instance;

    public static Cursa getInstance() {
        if (instance == null) instance = new Cursa();
        return instance;
    }

}