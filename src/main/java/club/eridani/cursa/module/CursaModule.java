package club.eridani.cursa.module;

import club.eridani.cursa.Cursa;
import club.eridani.cursa.event.events.client.InputUpdateEvent;
import club.eridani.cursa.event.events.network.PacketEvent;
import club.eridani.cursa.event.events.render.RenderOverlayEvent;
import club.eridani.cursa.event.events.render.RenderWorldEvent;
import club.eridani.cursa.setting.Setting;
import club.eridani.cursa.setting.settings.*;
import net.minecraft.client.Minecraft;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderWorldLastEvent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CursaModule {

    public final String name;
    public final Category category;
    public int keyCode;

    boolean enabled = false;
    private final List<Setting<?>> settings = new ArrayList<>();

    public List<Setting<?>> getSettings() {
        return settings;
    }

    public Minecraft mc = Minecraft.getMinecraft();

    public CursaModule() {
        this.name = getAnnotation().name();
        this.category = getAnnotation().category();
        this.keyCode = getAnnotation().keyCode();
    }

    @SafeVarargs
    public final <T> List<T> listOf(T... elements) {
        return Arrays.asList(elements);
    }

    public void toggle() {
        if (isEnabled()) disable();
        else enable();
    }

    public boolean isEnabled() {
        return enabled;
    }

    public boolean isDisabled() {
        return !enabled;
    }

    public void enable() {
        enabled = true;
        Cursa.MODULE_BUS.register(this);
        onEnable();
    }

    public void disable() {
        enabled = false;
        Cursa.MODULE_BUS.unregister(this);
        onDisable();
    }

    public void onEnable() {
    }

    public void onDisable() {
    }

    public void onTick() {
    }

    public void onRenderTick() {
    }

    public void onRender(RenderOverlayEvent event) {
    }

    public void onRenderWorld(RenderWorldEvent event) {
    }

    public void onInputUpdate(InputUpdateEvent event){
    }

    public void onPacketSend(PacketEvent.Send event) {
    }

    public void onPacketReceive(PacketEvent.Receive event) {
    }

    public void onSettingChange(Setting<?> setting) {
    }

    public Setting<Boolean> setting(String name, boolean defaultValue) {
        BooleanSetting value = new BooleanSetting(name, defaultValue);
        settings.add(value);
        return value;
    }

    public Setting<Integer> setting(String name, int defaultValue, int minValue, int maxValue) {
        IntSetting value = new IntSetting(name, defaultValue, minValue, maxValue);
        settings.add(value);
        return value;
    }

    public Setting<Float> setting(String name, float defaultValue, float minValue, float maxValue) {
        FloatSetting value = new FloatSetting(name, defaultValue, minValue, maxValue);
        settings.add(value);
        return value;
    }

    public Setting<Double> setting(String name, double defaultValue, double minValue, double maxValue) {
        DoubleSetting value = new DoubleSetting(name, defaultValue, minValue, maxValue);
        settings.add(value);
        return value;
    }

    public Setting<String> setting(String name, String defaultMode, List<String> modes) {
        ModeSetting value = new ModeSetting(name, defaultMode, modes);
        settings.add(value);
        return value;
    }

    public Setting<String> setting(String name, String defaultMode, String... modes) {
        ModeSetting value = new ModeSetting(name, defaultMode, Arrays.asList(modes));
        settings.add(value);
        return value;
    }

    private Module getAnnotation() {
        if (getClass().isAnnotationPresent(Module.class)) {
            return getClass().getAnnotation(Module.class);
        }
        throw new IllegalStateException("No Annotation on class " + this.getClass().getCanonicalName() + "!");
    }


}
