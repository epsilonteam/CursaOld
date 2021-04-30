package club.eridani.cursa.module.modules.client;

import club.eridani.cursa.module.Category;
import club.eridani.cursa.module.CursaModule;
import club.eridani.cursa.module.Module;
import club.eridani.cursa.setting.Setting;

@Module(name = "GUI", category = Category.CLIENT)
public class GUI extends CursaModule {

    public Setting<Boolean> rainbow = setting("Rainbow",false);
    public Setting<Float> rainbowSpeed = setting("Rainbow Speed", 1.0f,0.0f,30.0f).b(rainbow);
    public Setting<Float> rainbowSaturation = setting("Saturation",0.75f,0.0f,1.0f).b(rainbow);
    public Setting<Float> rainbowBrightness = setting("Brightness",0.8f,0.0f,1.0f).b(rainbow);
    public Setting<Integer> red = setting("Red",0,0,255).r(rainbow);
    public Setting<Integer> green = setting("Green",96,0,255).r(rainbow);
    public Setting<Integer> blue = setting("Blue",255,0,255).r(rainbow);
    public Setting<Integer> transparency = setting("Transparency",200,0,255);
    public Setting<Boolean> particle = setting("Particle",true);
    public Setting<String> background = setting("Background","Shadow",listOf("Shadow","Blur","Both","None"));

    @Override
    public void onEnable(){
        disable();
    }

}
