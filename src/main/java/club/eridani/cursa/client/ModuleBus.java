package club.eridani.cursa.client;

import club.eridani.cursa.Cursa;
import club.eridani.cursa.concurrent.task.VoidTask;
import club.eridani.cursa.concurrent.utils.Syncer;
import club.eridani.cursa.event.events.client.InputUpdateEvent;
import club.eridani.cursa.event.events.client.SettingUpdateEvent;
import club.eridani.cursa.event.events.client.TickEvent;
import club.eridani.cursa.event.events.network.PacketEvent;
import club.eridani.cursa.event.events.render.RenderOverlayEvent;
import club.eridani.cursa.event.events.render.RenderWorldEvent;
import club.eridani.cursa.event.system.Listener;
import club.eridani.cursa.module.ModuleBase;
import club.eridani.cursa.notification.NotificationManager;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import static club.eridani.cursa.concurrent.TaskManager.launch;
import static club.eridani.cursa.concurrent.TaskManager.runBlocking;

public class ModuleBus {

    public ModuleBus() {
        Cursa.EVENT_BUS.register(this);
    }

    private final List<ModuleBase> modules = new CopyOnWriteArrayList<>();

    public void register(ModuleBase module) {
        modules.add(module);
        Cursa.EVENT_BUS.register(module);
    }

    public void unregister(ModuleBase module) {
        modules.remove(module);
        Cursa.EVENT_BUS.unregister(module);
    }

    public boolean isRegistered(ModuleBase module) {
        return modules.contains(module);
    }

    public List<ModuleBase> getModules() {
        return modules;
    }

    @Listener
    public void onKey(InputUpdateEvent event) {
        modules.forEach(mod -> mod.onInputUpdate(event));
    }

    @Listener
    public void onTick(TickEvent event) {
        List<VoidTask> enabledTasks = new ArrayList<>();
        Syncer syncer = new Syncer();
        launch(syncer, () -> modules.forEach(it -> enabledTasks.add(() -> {
            try {
                it.onParallelTick();
            } catch (Exception exception) {
                NotificationManager.fatal("Error while running Parallel Tick!");
                exception.printStackTrace();
            }
        })));
        modules.forEach(it -> {
            try {
                it.onTick();
            } catch (Exception exception) {
                NotificationManager.fatal("Error while running Tick!");
                exception.printStackTrace();
            }
        });
        syncer.await();
        runBlocking(enabledTasks);
    }

    @Listener
    public void onRenderTick(RenderOverlayEvent event) {
        List<VoidTask> enabledTasks = new ArrayList<>();
        Syncer syncer = new Syncer();
        launch(syncer, () -> modules.forEach(it -> enabledTasks.add(() -> {
            try {
                it.onParallelRenderTick();
            } catch (Exception exception) {
                NotificationManager.fatal("Error while running RenderTick!");
                exception.printStackTrace();
            }
        })));
        modules.forEach(it -> {
            try {
                it.onRenderTick();
            } catch (Exception exception) {
                NotificationManager.fatal("Error while running Parallel RenderTick!");
                exception.printStackTrace();
            }
        });
        syncer.await();
        runBlocking(enabledTasks);
        modules.forEach(it -> {
            try {
                it.onRender(event);
            } catch (Exception exception) {
                NotificationManager.fatal("Error while running onRender!");
                exception.printStackTrace();
            }
        });
    }

    @Listener
    public void onRenderWorld(RenderWorldEvent event) {
        modules.forEach(it -> {
            try {
                it.onRenderWorld(event);
            } catch (Exception exception) {
                NotificationManager.fatal("Error while running onRenderWorld!");
                exception.printStackTrace();
            }
        });
    }

    @Listener
    public void onPacketSend(PacketEvent.Send event) {
        modules.forEach(it -> {
            try {
                it.onPacketSend(event);
            } catch (Exception exception) {
                NotificationManager.fatal("Error while running onPacketSend!");
                exception.printStackTrace();
            }
        });
    }

    @Listener
    public void onPacketReceive(PacketEvent.Receive event) {
        modules.forEach(it -> {
            try {
                it.onPacketReceive(event);
            } catch (Exception exception) {
                NotificationManager.fatal("Error while running onPacketReceive!");
                exception.printStackTrace();
            }
        });
    }

    @Listener
    public void onSettingChange(SettingUpdateEvent event) {
        modules.forEach(it -> {
            try {
                it.onSettingChange(event.getSetting());
            } catch (Exception exception) {
                NotificationManager.fatal("Error while running onSettingChange!");
                exception.printStackTrace();
            }
        });
    }

}
