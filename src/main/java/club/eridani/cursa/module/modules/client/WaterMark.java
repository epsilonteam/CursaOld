package club.eridani.cursa.module.modules.client;

import club.eridani.cursa.Cursa;
import club.eridani.cursa.client.FontManager;
import club.eridani.cursa.client.GUIManager;
import club.eridani.cursa.common.annotations.Module;
import club.eridani.cursa.common.annotations.Parallel;
import club.eridani.cursa.event.events.render.RenderOverlayEvent;
import club.eridani.cursa.module.Category;
import club.eridani.cursa.module.ModuleBase;
import club.eridani.cursa.setting.Setting;
import club.eridani.cursa.utils.ChatUtil;

import java.awt.*;

@Parallel
@Module(name = "WaterMark", category = Category.CLIENT)
public class WaterMark extends ModuleBase {

    private final Setting<Integer> x = setting("X", 0, 0, 3840);
    private final Setting<Integer> y = setting("Y", 0, 0, 2160);

    @Override
    public void onRender(RenderOverlayEvent event) {
        int color = GUIManager.isRainbow() ? rainbow(1) : GUIManager.getColor3I();
        FontManager.draw(Cursa.MOD_NAME + " " + ChatUtil.SECTIONSIGN + "f" + Cursa.MOD_VERSION, x.getValue() + 1, y.getValue() + 3, color);
    }

    public int rainbow(int delay) {
        double rainbowState = Math.ceil((System.currentTimeMillis() + delay) / 20.0);
        rainbowState %= 360;
        return Color.getHSBColor((float) (rainbowState / 360.0f), 1.0f, 1.0f).getRGB();
    }

}
