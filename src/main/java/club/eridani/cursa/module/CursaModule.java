package club.eridani.cursa.module;

import club.eridani.cursa.Cursa;
import club.eridani.cursa.event.events.network.PacketEvent;
import club.eridani.cursa.setting.Setting;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderWorldLastEvent;

public class CursaModule {

    public final String name;
    public final Category category;
    public final int keyCode;

    boolean enabled = false;

    public CursaModule(){
        this.name = getAnnotation().name();
        this.category = getAnnotation().category();
        this.keyCode = getAnnotation().keyCode();
    }

    public boolean isEnabled(){
        return enabled;
    }

    public boolean isDisabled(){
        return !enabled;
    }

    public void enable(){
        enabled = true;
        Cursa.MODULE_BUS.register(this);
        onEnable();
    }

    public void disable(){
        enabled = false;
        Cursa.MODULE_BUS.unregister(this);
        onDisable();
    }

    public void onEnable(){}
    public void onDisable(){}

    public void onTick(){}
    public void onRenderTick(){}
    public void onRender(RenderGameOverlayEvent.Post event){}
    public void onRenderWorld(RenderWorldLastEvent event){}
    public void onPacketSend(PacketEvent.Send event){}
    public void onPacketReceive(PacketEvent.Receive event){}
    public void onSettingChange(Setting<?> setting){}

    private Module getAnnotation() {
        if (getClass().isAnnotationPresent(Module.class)) {
            return getClass().getAnnotation(Module.class);
        }
        throw new IllegalStateException("No Annotation on class " + this.getClass().getCanonicalName() + "!");
    }


}
