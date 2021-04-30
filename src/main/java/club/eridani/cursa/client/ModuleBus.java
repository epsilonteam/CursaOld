package club.eridani.cursa.client;

import club.eridani.cursa.Cursa;
import club.eridani.cursa.event.events.client.SettingUpdateEvent;
import club.eridani.cursa.event.events.network.PacketEvent;
import club.eridani.cursa.module.CursaModule;
import club.eridani.cursa.event.system.Listener;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class ModuleBus {

    public ModuleBus(){
        Cursa.EVENT_BUS.register(this);
    }

    private final List<CursaModule> modules = new CopyOnWriteArrayList<>();

    public void register(CursaModule module){
        modules.add(module);
        Cursa.EVENT_BUS.register(module);
    }

    public void unregister(CursaModule module){
        modules.remove(module);
        Cursa.EVENT_BUS.unregister(module);
    }

    public List<CursaModule> getModules(){
        return modules;
    }

    @Listener
    public void onTick(TickEvent.ClientTickEvent event){
        modules.forEach(it ->{
            if(it.isEnabled()) it.onTick();
        });
    }

    @Listener
    public void onRenderTick(TickEvent.RenderTickEvent event){
        modules.forEach(it ->{
            if(it.isEnabled()) it.onRenderTick();
        });
    }

    @Listener
    public void onRender(RenderGameOverlayEvent.Post event){
        modules.forEach(it ->{
            if(it.isEnabled()) it.onRender(event);
        });
    }

    @Listener
    public void onRenderWorld(RenderWorldLastEvent event){
        modules.forEach(it ->{
            if(it.isEnabled()) it.onRenderWorld(event);
        });
    }

    @Listener
    public void onPacketSend(PacketEvent.Send event){
        modules.forEach(it ->{
            if(it.isEnabled()) it.onPacketSend(event);
        });
    }

    @Listener
    public void onPacketReceive(PacketEvent.Receive event){
        modules.forEach(it ->{
            if(it.isEnabled()) it.onPacketReceive(event);
        });
    }

    @Listener
    public void onSettingChange(SettingUpdateEvent event){
        modules.forEach(it ->{
            if(it.isEnabled()) it.onSettingChange(event.getSetting());
        });
    }

}
