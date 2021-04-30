package club.eridani.cursa.client;

import club.eridani.cursa.Cursa;
import club.eridani.cursa.event.events.client.InputUpdateEvent;
import club.eridani.cursa.event.events.client.SettingUpdateEvent;
import club.eridani.cursa.event.events.client.TickEvent;
import club.eridani.cursa.event.events.network.PacketEvent;
import club.eridani.cursa.event.events.render.RenderOverlayEvent;
import club.eridani.cursa.event.events.render.RenderWorldEvent;
import club.eridani.cursa.event.system.Listener;
import club.eridani.cursa.module.CursaModule;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class ModuleBus {

    public ModuleBus() {
        Cursa.EVENT_BUS.register(this);
    }

    private final List<CursaModule> modules = new CopyOnWriteArrayList<>();

    public void register(CursaModule module) {
        modules.add(module);
        Cursa.EVENT_BUS.register(module);
    }

    public void unregister(CursaModule module) {
        modules.remove(module);
        Cursa.EVENT_BUS.unregister(module);
    }

    public List<CursaModule> getModules() {
        return modules;
    }

    @Listener
    public void onKey(InputUpdateEvent event) {
        modules.forEach(mod -> mod.onInputUpdate(event));
    }

    @Listener
    public void onTick(TickEvent event) {
        modules.forEach(CursaModule::onTick);
    }

    @Listener
    public void onRenderTick(RenderOverlayEvent event) {
        modules.forEach(CursaModule::onRenderTick);
    }

    @Listener
    public void onRender(RenderOverlayEvent event) {
        modules.forEach(it -> it.onRender(event));
    }

    @Listener
    public void onRenderWorld(RenderWorldEvent event) {
        modules.forEach(it -> it.onRenderWorld(event));
    }

    @Listener
    public void onPacketSend(PacketEvent.Send event) {
        modules.forEach(it -> it.onPacketSend(event));
    }

    @Listener
    public void onPacketReceive(PacketEvent.Receive event) {
        modules.forEach(it -> it.onPacketReceive(event));
    }

    @Listener
    public void onSettingChange(SettingUpdateEvent event) {
        modules.forEach(it -> it.onSettingChange(event.getSetting()));
    }

}
