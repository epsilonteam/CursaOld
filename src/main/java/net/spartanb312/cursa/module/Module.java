package net.spartanb312.cursa.module;

import net.spartanb312.cursa.Cursa;
import net.spartanb312.cursa.common.annotations.ModuleInfo;
import net.spartanb312.cursa.common.annotations.Parallel;
import net.spartanb312.cursa.concurrent.decentralization.ListenableImpl;
import net.spartanb312.cursa.concurrent.task.VoidTask;
import net.spartanb312.cursa.event.events.client.InputUpdateEvent;
import net.spartanb312.cursa.event.events.network.PacketEvent;
import net.spartanb312.cursa.event.events.render.RenderEvent;
import net.spartanb312.cursa.event.events.render.RenderOverlayEvent;
import net.spartanb312.cursa.notification.NotificationManager;
import net.spartanb312.cursa.setting.Setting;
import net.spartanb312.cursa.utils.ChatUtil;
import net.spartanb312.cursa.utils.KeyBind;
import net.minecraft.client.Minecraft;
import net.spartanb312.cursa.setting.settings.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Module extends ListenableImpl {

    public final String name = getAnnotation().name();
    public final Category category = getAnnotation().category();
    public final Parallel annotation = getClass().getAnnotation(Parallel.class);
    public final boolean parallelRunnable = annotation != null && annotation.runnable();
    public final String description = getAnnotation().description();

    boolean enabled = false;
    private final List<Setting<?>> settings = new ArrayList<>();

    public List<Setting<?>> getSettings() {
        return settings;
    }

    public Minecraft mc = Minecraft.getMinecraft();

    public final KeyBind keyBind = new KeyBind(getAnnotation().keyCode(), this::toggle);

    private final Setting<KeyBind> bindSetting = setting("Bind", keyBind, "The key bind of this module");
    private final Setting<String> visibleSetting = setting("Visible", getAnnotation().visible() ? "True" : "False", listOf("True", "False"), "Show on active module list or not");
    private final Setting<VoidTask> reset = setting("Reset", () -> {
        disable();
        settings.forEach(Setting::reset);
    }, "Reset this module");

    @SafeVarargs
    public final <T> List<T> listOf(T... elements) {
        return Arrays.asList(elements);
    }

    public void toggle() {
        if (isEnabled()) disable();
        else enable();
    }

    public void reload() {
        if (enabled) {
            enabled = false;
            Cursa.MODULE_BUS.unregister(this);
            onDisable();
            enabled = true;
            Cursa.MODULE_BUS.register(this);
            onEnable();
        }
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
        subscribe();
        NotificationManager.moduleToggle(this, true);
        onEnable();
    }

    public void disable() {
        enabled = false;
        Cursa.MODULE_BUS.unregister(this);
        unsubscribe();
        NotificationManager.moduleToggle(this, false);
        onDisable();
    }

    public void onPacketReceive(PacketEvent.Receive event) {
    }

    public void onPacketSend(PacketEvent.Send event) {
    }

    public void onTick() {
    }

    public void onRenderTick() {
    }

    public void onEnable() {
    }

    public void onDisable() {
    }

    public void onRender(RenderOverlayEvent event) {
    }

    public void onRenderWorld(RenderEvent event) {
    }

    public void onInputUpdate(InputUpdateEvent event) {
    }

    public void onSettingChange(Setting<?> setting) {
    }

    public Setting<VoidTask> setting(String name, VoidTask defaultValue) {
        ActionSetting setting = new ActionSetting(name, defaultValue);
        settings.add(setting);
        return setting;
    }

    public Setting<KeyBind> setting(String name, KeyBind defaultValue) {
        BindSetting setting = new BindSetting(name, defaultValue);
        settings.add(setting);
        return setting;
    }

    public Setting<Boolean> setting(String name, boolean defaultValue) {
        BooleanSetting setting = new BooleanSetting(name, defaultValue);
        settings.add(setting);
        return setting;
    }

    public Setting<Integer> setting(String name, int defaultValue, int minValue, int maxValue) {
        IntSetting setting = new IntSetting(name, defaultValue, minValue, maxValue);
        settings.add(setting);
        return setting;
    }

    public Setting<Float> setting(String name, float defaultValue, float minValue, float maxValue) {
        FloatSetting setting = new FloatSetting(name, defaultValue, minValue, maxValue);
        settings.add(setting);
        return setting;
    }

    public Setting<Double> setting(String name, double defaultValue, double minValue, double maxValue) {
        DoubleSetting setting = new DoubleSetting(name, defaultValue, minValue, maxValue);
        settings.add(setting);
        return setting;
    }

    public Setting<String> setting(String name, String defaultMode, List<String> modes) {
        ModeSetting setting = new ModeSetting(name, defaultMode, modes);
        settings.add(setting);
        return setting;
    }

    public Setting<String> setting(String name, String defaultMode, String... modes) {
        ModeSetting setting = new ModeSetting(name, defaultMode, Arrays.asList(modes));
        settings.add(setting);
        return setting;
    }

    public Setting<VoidTask> setting(String name, VoidTask defaultValue, String description) {
        Setting<VoidTask> setting = new ActionSetting(name, defaultValue).des(description);
        settings.add(setting);
        return setting;
    }

    public Setting<KeyBind> setting(String name, KeyBind defaultValue, String description) {
        Setting<KeyBind> setting = new BindSetting(name, defaultValue).des(description);
        settings.add(setting);
        return setting;
    }

    public Setting<Boolean> setting(String name, boolean defaultValue, String description) {
        Setting<Boolean> setting = new BooleanSetting(name, defaultValue).des(description);
        settings.add(setting);
        return setting;
    }

    public Setting<Integer> setting(String name, int defaultValue, int minValue, int maxValue, String description) {
        Setting<Integer> setting = new IntSetting(name, defaultValue, minValue, maxValue).des(description);
        settings.add(setting);
        return setting;
    }

    public Setting<Float> setting(String name, float defaultValue, float minValue, float maxValue, String description) {
        Setting<Float> setting = new FloatSetting(name, defaultValue, minValue, maxValue).des(description);
        settings.add(setting);
        return setting;
    }

    public Setting<Double> setting(String name, double defaultValue, double minValue, double maxValue, String description) {
        Setting<Double> setting = new DoubleSetting(name, defaultValue, minValue, maxValue).des(description);
        settings.add(setting);
        return setting;
    }

    public Setting<String> setting(String name, String defaultMode, List<String> modes, String description) {
        Setting<String> setting = new ModeSetting(name, defaultMode, modes).des(description);
        settings.add(setting);
        return setting;
    }

    public Setting<String> setting(String name, String defaultMode, String description, String... modes) {
        Setting<String> setting = new ModeSetting(name, defaultMode, Arrays.asList(modes)).des(description);
        settings.add(setting);
        return setting;
    }

    private ModuleInfo getAnnotation() {
        if (getClass().isAnnotationPresent(ModuleInfo.class)) {
            return getClass().getAnnotation(ModuleInfo.class);
        }
        throw new IllegalStateException("No Annotation on class " + this.getClass().getCanonicalName() + "!");
    }

    public String getModuleInfo() {
        return "";
    }

    public String getHudSuffix() {
        return this.name + (!this.getModuleInfo().equals("") ? (ChatUtil.colored("7") + "[" + ChatUtil.colored("f") + this.getModuleInfo() + ChatUtil.colored("7") + "]") : this.getModuleInfo());
    }

}
