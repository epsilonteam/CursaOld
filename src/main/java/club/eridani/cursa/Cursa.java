package club.eridani.cursa;

import club.eridani.cursa.client.ConfigManager;
import club.eridani.cursa.client.ModuleBus;
import club.eridani.cursa.event.events.client.InitializationEvent;
import club.eridani.cursa.managers.ModuleManager;
import club.eridani.cursa.event.system.EventManager;
import club.eridani.cursa.event.system.Listener;
import club.eridani.cursa.event.system.impl.annotated.AnnotatedEventManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Cursa {

    public static final String MOD_NAME = "Cursa";
    public static final String MOD_ID = "cursa";
    public static final String MOD_VERSION = "b1";

    public static final Logger log = LogManager.getLogger(MOD_NAME);

    public Cursa(){
        instance = this;
        EVENT_BUS.register(this);
    }

    @Listener
    public void preInitialize(InitializationEvent.PreInitialize event){

    }

    @Listener
    public void initialize(InitializationEvent.Initialize event){
        ModuleManager.init();
    }

    @Listener
    public void postInitialize(InitializationEvent.PostInitialize event){
        ConfigManager.init();
    }

    public static EventManager EVENT_BUS = new AnnotatedEventManager();
    public static ModuleBus MODULE_BUS = new ModuleBus();

    private static Cursa instance;

    public static Cursa getInstance(){
        if(instance == null) instance = new Cursa();
        return instance;
    }

}