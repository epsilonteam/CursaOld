package club.eridani.cursa.client;

import club.eridani.cursa.Cursa;
import club.eridani.cursa.common.annotations.PacketListener;
import club.eridani.cursa.common.annotations.ParallelLoadable;
import club.eridani.cursa.common.annotations.ParallelRunnable;
import club.eridani.cursa.common.annotations.TickUpdate;
import club.eridani.cursa.common.types.Tick;
import club.eridani.cursa.concurrent.task.EventTask;
import club.eridani.cursa.concurrent.task.VoidTask;
import club.eridani.cursa.event.events.client.KeyEvent;
import club.eridani.cursa.event.events.network.PacketEvent;
import club.eridani.cursa.event.system.Listener;
import club.eridani.cursa.module.ModuleBase;
import club.eridani.cursa.utils.ClassUtil;
import club.eridani.cursa.utils.math.Pair;
import net.minecraft.network.Packet;

import java.lang.reflect.Method;
import java.util.*;

import static club.eridani.cursa.concurrent.TaskManager.runBlocking;

public class ModuleManager {

    public Map<Class<? extends ModuleBase>, ModuleBase> moduleMap = new HashMap<>();
    public final List<ModuleBase> moduleList = new ArrayList<>();
    public final Map<ModuleBase, Pair<Tick, VoidTask>> parallelTasks = new HashMap<>();
    public final Map<ModuleBase, Pair<Tick, VoidTask>> tickUpdateTasks = new HashMap<>();
    public final Map<ModuleBase, Pair<EventTask<PacketEvent.Send>, Class<? extends Packet<?>>>> packetSendListeners = new HashMap<>();
    public final Map<ModuleBase, Pair<EventTask<PacketEvent.Receive>, Class<? extends Packet<?>>>> packetReceiveListeners = new HashMap<>();

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
        Set<Class<? extends ModuleBase>> classList = ClassUtil.findClasses(ModuleBase.class.getPackage().getName(), ModuleBase.class);
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
        Cursa.log.fatal("[ModuleManager]Loading listeners.");
        sortAndPut();
        checkParallel();
        checkUpdate();
        registerPacketListener();
        Cursa.log.info("[ModuleManager]Loaded " + "\n" +
                moduleList.size() + " modules. " + "\n" +
                parallelTasks.size() + " ParallelTask Listeners. " + "\n" +
                packetSendListeners.size() + " PacketSend Listeners. " + "\n" +
                packetReceiveListeners.size() + " PacketReceive Listeners. "
        );
    }

    private void sortAndPut() {
        moduleList.sort(Comparator.comparing(it -> it.name));
        moduleList.forEach(it -> moduleMap.put(it.getClass(), it));
    }

    private void checkUpdate() {
        moduleList.forEach(it -> {
            for (Method method : it.getClass().getDeclaredMethods()) {
                if (method.isAnnotationPresent(TickUpdate.class)
                        && method.getParameterCount() == 0
                ) {
                    TickUpdate annotation = method.getAnnotation(TickUpdate.class);
                    if (!method.isAccessible()) method.setAccessible(true);
                    ModuleManager.getInstance().tickUpdateTasks.put(it, new Pair<>(annotation.type(), () -> {
                        try {
                            method.invoke(it);
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    }));
                }
            }
        });
    }

    private void checkParallel() {
        moduleList.forEach(it -> {
            for (Method method : it.getClass().getDeclaredMethods()) {
                if (method.isAnnotationPresent(ParallelRunnable.class)
                        && method.getParameterCount() == 0
                ) {
                    ParallelRunnable annotation = method.getAnnotation(ParallelRunnable.class);
                    if (!method.isAccessible()) method.setAccessible(true);
                    parallelTasks.put(it, new Pair<>(annotation.type(), () -> {
                        try {
                            method.invoke(it);
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    }));
                }
            }
        });
    }

    private void registerPacketListener() {
        moduleList.forEach(it -> {
            for (Method method : it.getClass().getDeclaredMethods()) {
                if (method.isAnnotationPresent(PacketListener.class) && method.getParameterCount() == 1) {
                    Class<?> clazz = method.getParameters()[0].getType();
                    if (clazz.isAssignableFrom(PacketEvent.Send.class)
                            || clazz.isAssignableFrom(PacketEvent.Receive.class)) {
                        PacketListener annotation = method.getAnnotation(PacketListener.class);
                        if (!method.isAccessible()) method.setAccessible(true);
                        switch (annotation.channel()) {
                            case Send: {
                                packetSendListeners.put(it, new Pair<>(valueIn -> {
                                    try {
                                        method.invoke(it, valueIn);
                                    } catch (Exception ex) {
                                        ex.printStackTrace();
                                    }
                                }, annotation.target()));
                                break;
                            }
                            case Receive: {
                                packetReceiveListeners.put(it, new Pair<>(valueIn -> {
                                    try {
                                        method.invoke(it, valueIn);
                                    } catch (Exception ex) {
                                        ex.printStackTrace();
                                    }
                                }, annotation.target()));
                                break;
                            }
                        }
                    }
                }
            }
        });
    }

}
