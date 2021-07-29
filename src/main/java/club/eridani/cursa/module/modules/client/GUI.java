package club.eridani.cursa.module.modules.client;

import club.eridani.cursa.common.annotations.Module;
import club.eridani.cursa.common.annotations.Parallel;
import club.eridani.cursa.module.Category;
import club.eridani.cursa.module.ModuleBase;
import club.eridani.cursa.setting.Setting;

@Parallel
@Module(name = "GUI", category = Category.CLIENT)
public class GUI extends ModuleBase {

    public Setting<Boolean> rainbow = setting("Rainbow",false);
    public Setting<Float> rainbowSpeed = setting("Rainbow Speed", 1.0f,0.0f,30.0f).whenTrue(rainbow);
    public Setting<Float> rainbowSaturation = setting("Saturation",0.75f,0.0f,1.0f).whenTrue(rainbow);
    public Setting<Float> rainbowBrightness = setting("Brightness",0.8f,0.0f,1.0f).whenTrue(rainbow);
    public Setting<Integer> red = setting("Red",70,0,255).whenFalse(rainbow);
    public Setting<Integer> green = setting("Green",170,0,255).whenFalse(rainbow);
    public Setting<Integer> blue = setting("Blue",255,0,255).whenFalse(rainbow);
    public Setting<Integer> transparency = setting("Transparency",200,0,255);
    public Setting<Boolean> particle = setting("Particle",true);
    public Setting<String> background = setting("Background","Shadow",listOf("Shadow","Blur","Both","None"));

    @Override
    public void onEnable(){
        disable();
    }

}
