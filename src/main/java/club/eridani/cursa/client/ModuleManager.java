package club.eridani.cursa.client;

import club.eridani.cursa.Cursa;
import club.eridani.cursa.event.events.client.KeyEvent;
import club.eridani.cursa.event.system.Listener;
import club.eridani.cursa.module.ModuleBase;
import club.eridani.cursa.utils.ClassUtil;

import java.util.*;

public class ModuleManager {

    public Map<Class<? extends ModuleBase>, ModuleBase> moduleMap = new HashMap<>();

    private static ModuleManager instance;

    public static List<ModuleBase> getModules() {
        return new ArrayList<>(getInstance().moduleMap.values());
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
        Cursa.log.fatal("Module " + targetName + " is not exist.Please check twice!");
        return null;
    }

    private void loadModules() {
        List<ModuleBase> tempList = new ArrayList<>();
        Set<Class<? extends ModuleBase>> classList = ClassUtil.findClasses(ModuleBase.class.getPackage().getName(), ModuleBase.class);
        classList.stream().sorted(Comparator.comparing(Class::getSimpleName)).forEach(clazz -> {
            try {
                ModuleBase module = clazz.newInstance();
                tempList.add(module);
            } catch (Exception e) {
                e.printStackTrace();
                System.err.println("Couldn't initiate Module " + clazz.getSimpleName() + "! Error: " + e.getClass().getSimpleName() + ", message: " + e.getMessage());
            }
        });
        tempList.sort(Comparator.comparing(it -> it.name));
        tempList.forEach(it -> {
            moduleMap.put(it.getClass(), it);
        });
    }

}
