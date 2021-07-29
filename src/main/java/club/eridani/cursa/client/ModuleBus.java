package club.eridani.cursa.client;

import club.eridani.cursa.Cursa;
import club.eridani.cursa.event.events.client.InputUpdateEvent;
import club.eridani.cursa.event.events.client.SettingUpdateEvent;
import club.eridani.cursa.event.events.client.TickEvent;
import club.eridani.cursa.event.events.network.PacketEvent;
import club.eridani.cursa.event.events.render.RenderOverlayEvent;
import club.eridani.cursa.event.events.render.RenderWorldEvent;
import club.eridani.cursa.event.system.Listener;
import club.eridani.cursa.module.ModuleBase;
import club.eridani.cursa.notification.NotificationManager;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import static club.eridani.cursa.concurrent.TaskManager.runBlocking;


public class ModuleBus {

    public ModuleBus() {
        Cursa.EVENT_BUS.register(this);
    }

    private final List<ModuleBase> modules = new CopyOnWriteArrayList<>();

    public synchronized void register(ModuleBase module) {
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
        runBlocking(it -> modules.forEach(module -> {
            if (module.parallelRunnable) {
                it.launch(() -> {
                    try {
                        module.onTick();
                    } catch (Exception exception) {
                        NotificationManager.fatal("Error while running Parallel Tick!");
                        exception.printStackTrace();
                    }
                });
            } else {
                try {
                    module.onTick();
                } catch (Exception exception) {
                    NotificationManager.fatal("Error while running Tick!");
                    exception.printStackTrace();
                }
            }
        }));
    }

    @Listener
    public void onRenderTick(RenderOverlayEvent event) {
        runBlocking(it -> modules.forEach(module -> {
            try {
                module.onRender(event);
            } catch (Exception exception) {
                NotificationManager.fatal("Error while running onRender!");
                exception.printStackTrace();
            }
            if (module.parallelRunnable) {
                it.launch(() -> {
                    try {
                        module.onRenderTick();
                    } catch (Exception exception) {
                        NotificationManager.fatal("Error while running Parallel Render Tick!");
                        exception.printStackTrace();
                    }
                });
            } else {
                try {
                    module.onRenderTick();
                } catch (Exception exception) {
                    NotificationManager.fatal("Error while running Render Tick!");
                    exception.printStackTrace();
                }
            }
        }));
    }

    @Listener
    public void onRenderWorld(RenderWorldEvent event) {
        WorldRenderPatcher.INSTANCE.patch(event);
    }

    @Listener
    public void onPacketSend(PacketEvent.Send event) {
        modules.forEach(module ->{
            try {
                module.onPacketSend(event);
            } catch (Exception exception) {
                NotificationManager.fatal("Error while running PacketSend!");
                exception.printStackTrace();
            }
        });
    }

    @Listener
    public void onPacketReceive(PacketEvent.Receive event) {
       modules.forEach(module ->{
           try {
               module.onPacketReceive(event);
           } catch (Exception exception) {
               NotificationManager.fatal("Error while running PacketReceive!");
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
