package club.eridani.cursa.client;

import club.eridani.cursa.Cursa;
import club.eridani.cursa.common.types.Packets;
import club.eridani.cursa.common.types.Tick;
import club.eridani.cursa.concurrent.task.VoidTask;
import club.eridani.cursa.concurrent.utils.Syncer;
import club.eridani.cursa.event.events.client.*;
import club.eridani.cursa.event.events.network.PacketEvent;
import club.eridani.cursa.event.events.render.RenderOverlayEvent;
import club.eridani.cursa.event.events.render.RenderWorldEvent;
import club.eridani.cursa.event.system.Listener;
import club.eridani.cursa.module.ModuleBase;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import static club.eridani.cursa.concurrent.TaskManager.*;

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
        launch(syncer, () -> ModuleManager.getInstance().parallelTasks.forEach((module, pair) -> {
            if (module.isEnabled() && pair.a.equals(Tick.Client)) enabledTasks.add(pair.b);
        }));
        ModuleManager.getInstance().tickUpdateTasks.forEach((module, pair) -> {
            if (module.isEnabled() && pair.a.equals(Tick.Client)) pair.b.invoke();
        });
        syncer.await();
        runBlocking(enabledTasks);
    }

    @Listener
    public void onRenderTick(RenderOverlayEvent event) {
        List<VoidTask> enabledTasks = new ArrayList<>();
        Syncer syncer = new Syncer();
        launch(syncer, () -> ModuleManager.getInstance().parallelTasks.forEach((module, pair) -> {
            if (module.isEnabled() && pair.a.equals(Tick.Render)) enabledTasks.add(pair.b);
        }));
        ModuleManager.getInstance().tickUpdateTasks.forEach((module, pair) -> {
            if (module.isEnabled() && pair.a.equals(Tick.Render)) pair.b.invoke();
        });
        syncer.await();
        runBlocking(enabledTasks);
    }

    @Listener
    public void onGameLoop(GameLoopEvent event) {
        List<VoidTask> enabledTasks = new ArrayList<>();
        Syncer syncer = new Syncer();
        launch(syncer, () -> ModuleManager.getInstance().parallelTasks.forEach((module, pair) -> {
            if (module.isEnabled() && pair.a.equals(Tick.Loop)) enabledTasks.add(pair.b);
        }));
        ModuleManager.getInstance().tickUpdateTasks.forEach((module, pair) -> {
            if (module.isEnabled() && pair.a.equals(Tick.Loop)) pair.b.invoke();
        });
        syncer.await();
        runBlocking(enabledTasks);
    }

    @Listener
    public void onRender(RenderOverlayEvent event) {
        modules.forEach(it -> it.onRender(event));
    }

    @Listener
    public void onRenderWorld(RenderWorldEvent event) {
        List<VoidTask> enabledTasks = new ArrayList<>();
        Syncer syncer = new Syncer();
        launch(syncer, () -> ModuleManager.getInstance().parallelTasks.forEach((module, pair) -> {
            if (module.isEnabled() && pair.a.equals(Tick.RenderWorld)) enabledTasks.add(pair.b);
        }));
        ModuleManager.getInstance().tickUpdateTasks.forEach((module, pair) -> {
            if (module.isEnabled() && pair.a.equals(Tick.RenderWorld)) pair.b.invoke();
        });
        modules.forEach(it -> it.onRenderWorld(event));
        syncer.await();
        runBlocking(enabledTasks);
    }

    @Listener
    public void onPacketSend(PacketEvent.Send event) {
        ModuleManager.getInstance().packetSendListeners.forEach((module, pair) -> {
            if (module.isEnabled() && (event.packet.getClass().equals(pair.b) || pair.b.equals(Packets.class))) {
                pair.a.invoke(event);
            }
        });
    }

    @Listener
    public void onPacketReceive(PacketEvent.Receive event) {
        ModuleManager.getInstance().packetReceiveListeners.forEach((module, pair) -> {
            if (module.isEnabled() && (event.packet.getClass().equals(pair.b) || pair.b.equals(Packets.class))) {
                pair.a.invoke(event);
            }
        });
    }

    @Listener
    public void onSettingChange(SettingUpdateEvent event) {
        modules.forEach(it -> it.onSettingChange(event.getSetting()));
    }

}
