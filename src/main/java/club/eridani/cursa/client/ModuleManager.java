package club.eridani.cursa.client;

import club.eridani.cursa.Cursa;
import club.eridani.cursa.event.events.client.KeyEvent;
import club.eridani.cursa.event.system.Listener;
import club.eridani.cursa.module.CursaModule;
import club.eridani.cursa.utils.ClassUtil;

import java.util.*;

public class ModuleManager {

    public Map<Class<? extends CursaModule>, CursaModule> moduleMap = new HashMap<>();

    private static ModuleManager instance;

    public static List<CursaModule> getModules() {
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

    public static CursaModule getModule(Class<? extends CursaModule> clazz) {
        return getInstance().moduleMap.get(clazz);
    }

    public static CursaModule getModuleByName(String targetName) {
        for (CursaModule module : getModules()) {
            if (module.name.equalsIgnoreCase(targetName)) {
                return module;
            }
        }
        Cursa.log.fatal("Module " + targetName + " is not exist.Please check twice!");
        return null;
    }

    private void loadModules() {
        List<CursaModule> tempList = new ArrayList<>();
        Set<Class<? extends CursaModule>> classList = ClassUtil.findClasses(CursaModule.class.getPackage().getName(), CursaModule.class);
        classList.stream().sorted(Comparator.comparing(Class::getSimpleName)).forEach(clazz -> {
            try {
                CursaModule module = clazz.newInstance();
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
