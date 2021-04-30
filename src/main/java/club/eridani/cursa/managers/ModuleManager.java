package club.eridani.cursa.managers;

import club.eridani.cursa.module.CursaModule;
import club.eridani.cursa.utils.ClassUtil;

import java.util.*;

public class ModuleManager {

    public Map<Class<? extends CursaModule>, CursaModule> moduleMap = new HashMap<>();

    private static ModuleManager instance;

    public static void init() {
        if (instance == null) instance = new ModuleManager();
        instance.moduleMap.clear();
        instance.loadModules();
    }

    public static ModuleManager getInstance() {
        if (instance == null) instance = new ModuleManager();
        return instance;
    }

    public static CursaModule getModule(Class<? extends CursaModule> clazz) {
        return getInstance().moduleMap.get(clazz);
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
