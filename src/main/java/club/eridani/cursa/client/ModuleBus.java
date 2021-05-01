package club.eridani.cursa.client;

import club.eridani.cursa.Cursa;
import club.eridani.cursa.concurrent.utils.Syncer;
import club.eridani.cursa.event.events.client.InputUpdateEvent;
import club.eridani.cursa.event.events.client.SettingUpdateEvent;
import club.eridani.cursa.event.events.client.TickEvent;
import club.eridani.cursa.event.events.network.PacketEvent;
import club.eridani.cursa.event.events.render.RenderOverlayEvent;
import club.eridani.cursa.event.events.render.RenderWorldEvent;
import club.eridani.cursa.event.system.Listener;
import club.eridani.cursa.module.ModuleBase;

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
        runBlocking(blocking -> {
            Syncer syncer = new Syncer(modules.size());
            modules.forEach(it -> launch(syncer, launching -> it.onParallelTick()));
            syncer.await();
        });
        modules.forEach(ModuleBase::onTick);
    }

    @Listener
    public void onRenderTick(RenderOverlayEvent event) {
        runBlocking(blocking -> {
            Syncer syncer = new Syncer(modules.size());
            modules.forEach(it -> launch(syncer, launching -> it.onParallelRenderTick()));
            syncer.await();
        });
        modules.forEach(ModuleBase::onRenderTick);
    }

    @Listener
    public void onRender(RenderOverlayEvent event) {
        runBlocking(blocking -> {
            Syncer syncer = new Syncer(modules.size());
            modules.forEach(it -> launch(syncer, launching -> it.onParallelTick()));
            syncer.await();
        });
        modules.forEach(it -> it.onRender(event));
    }

    @Listener
    public void onRenderWorld(RenderWorldEvent event) {
        modules.forEach(it -> it.onRenderWorld(event));
    }

    @Listener
    public void onPacketSend(PacketEvent.Send event) {
        runBlocking(blocking -> {
            Syncer syncer = new Syncer(modules.size());
            modules.forEach(it -> launch(syncer, it::onParallelPacketSend,event));
            syncer.await();
        });
        modules.forEach(it -> it.onPacketSend(event));
    }

    @Listener
    public void onPacketReceive(PacketEvent.Receive event) {
        runBlocking(blocking -> {
            Syncer syncer = new Syncer(modules.size());
            modules.forEach(it -> launch(syncer, it::onParallelPacketReceive,event));
            syncer.await();
        });
        modules.forEach(it -> it.onPacketReceive(event));
    }

    @Listener
    public void onSettingChange(SettingUpdateEvent event) {
        runBlocking(blocking -> {
            Syncer syncer = new Syncer(modules.size());
            modules.forEach(it -> launch(syncer, it::onParallelSettingChange,event.getSetting()));
            syncer.await();
        });
        modules.forEach(it -> it.onSettingChange(event.getSetting()));
    }

}
