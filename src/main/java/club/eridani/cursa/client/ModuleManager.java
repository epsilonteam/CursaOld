package club.eridani.cursa.client;

import club.eridani.cursa.Cursa;
import club.eridani.cursa.common.annotations.ParallelLoadable;
import club.eridani.cursa.concurrent.task.VoidTask;
import club.eridani.cursa.event.events.client.KeyEvent;
import club.eridani.cursa.event.system.Listener;
import club.eridani.cursa.module.ModuleBase;
import club.eridani.cursa.utils.ClassUtil;

import java.util.*;

import static club.eridani.cursa.concurrent.TaskManager.runBlocking;

public class ModuleManager {

    public Map<Class<? extends ModuleBase>, ModuleBase> moduleMap = new HashMap<>();
    public final List<ModuleBase> moduleList = new ArrayList<>();

    private static ModuleManager instance;

    public static List<ModuleBase> getModules() {
        return getInstance().moduleList;
    }

    public static void init() {
        if (instance == null) instance = new ModuleManager();
        instance.moduleMap.clear();
        instance.loadModules();
        Cursa.EVENT_BUS.register(instance);
    }

    @Listener
    public void onKey(KeyEvent event) {
        moduleMap.values().forEach(it -> {
            if (event.getKey() == it.keyCode) it.toggle();
        });
    }

    public static ModuleManager getInstance() {
        if (instance == null) instance = new ModuleManager();
        return instance;
    }

    public static ModuleBase getModule(Class<? extends ModuleBase> clazz) {
        return getInstance().moduleMap.get(clazz);
    }

    public static ModuleBase getModuleByName(String targetName) {
        for (ModuleBase module : getModules()) {
            if (module.name.equalsIgnoreCase(targetName)) {
                return module;
            }
        }
        Cursa.log.info("Module " + targetName + " is not exist.Please check twice!");
        return null;
    }

    private void loadModules() {
        Set<Class<? extends ModuleBase>> classList = ClassUtil.findClasses(Cursa.class.getPackage().getName(), ModuleBase.class);
        List<VoidTask> quickLoadList = new ArrayList<>();
        Cursa.log.info("[ModuleManager]Loading modules.");
        classList.stream().sorted(Comparator.comparing(Class::getSimpleName)).forEach(clazz -> {
            try {
                if (clazz.isAnnotationPresent(ParallelLoadable.class)) {
                    quickLoadList.add(() -> {
                        try {
                            ModuleBase module = clazz.newInstance();
                            synchronized (moduleList) {
                                moduleList.add(module);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            System.err.println("Couldn't initiate Module " + clazz.getSimpleName() + "! Error: " + e.getClass().getSimpleName() + ", message: " + e.getMessage());
                        }
                    });
                } else {
                    ModuleBase module = clazz.newInstance();
                    synchronized (moduleList) {
                        moduleList.add(module);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                System.err.println("Couldn't initiate Module " + clazz.getSimpleName() + "! Error: " + e.getClass().getSimpleName() + ", message: " + e.getMessage());
            }
        });
        runBlocking(quickLoadList);
        sortAndPut();
        Cursa.log.info("[ModuleManager]Loaded " + moduleList.size() + " modules");
    }

    private void sortAndPut() {
        moduleList.sort(Comparator.comparing(it -> it.name));
        moduleList.forEach(it -> moduleMap.put(it.getClass(), it));
    }


}
