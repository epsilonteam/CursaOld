package net.spartanb312.cursa.module.modules.client;

import net.spartanb312.cursa.common.annotations.ModuleInfo;
import net.spartanb312.cursa.common.annotations.Parallel;
import net.spartanb312.cursa.module.Category;
import net.spartanb312.cursa.module.Module;
import net.spartanb312.cursa.setting.Setting;

@Parallel
@ModuleInfo(name = "GUISetting", category = Category.CLIENT, description = "Settings of GUI")
public class GUISetting extends Module {

    public static GUISetting instance;

    public GUISetting() {
        instance = this;
    }

    public Setting<Boolean> rainbow = setting("Rainbow", false, "Rainbow color");
    public Setting<Float> rainbowSpeed = setting("Rainbow Speed", 1.0f, 0.0f, 30.0f, "Rainbow color change speed").whenTrue(rainbow);
    public Setting<Float> rainbowSaturation = setting("Saturation", 0.75f, 0.0f, 1.0f, "Rainbow color saturation").whenTrue(rainbow);
    public Setting<Float> rainbowBrightness = setting("Brightness", 0.8f, 0.0f, 1.0f, "Rainbow color brightness").whenTrue(rainbow);
    public Setting<Integer> red = setting("Red", 70, 0, 255, "Red").whenFalse(rainbow);
    public Setting<Integer> green = setting("Green", 170, 0, 255, "Green").whenFalse(rainbow);
    public Setting<Integer> blue = setting("Blue", 255, 0, 255, "Blue").whenFalse(rainbow);
    public Setting<Integer> transparency = setting("Transparency", 200, 0, 255, "The transparency(Alpha)");
    public Setting<Boolean> particle = setting("Particle", true, "Display particles on background");
    public Setting<String> background = setting("BG", "Shadow", listOf("Shadow", "Blur", "Both", "None"), "Background effect");

    @Override
    public void onEnable() {
        disable();
    }

}
