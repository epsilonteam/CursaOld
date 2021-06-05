package club.eridani.cursa.module.modules.client;

import club.eridani.cursa.Cursa;
import club.eridani.cursa.client.FontManager;
import club.eridani.cursa.client.GUIManager;
import club.eridani.cursa.common.annotations.Module;
import club.eridani.cursa.common.annotations.ParallelLoadable;
import club.eridani.cursa.event.events.render.RenderOverlayEvent;
import club.eridani.cursa.event.system.Listener;
import club.eridani.cursa.module.Category;
import club.eridani.cursa.module.ModuleBase;
import club.eridani.cursa.setting.Setting;
import club.eridani.cursa.utils.RenderHelper;

import java.awt.*;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@ParallelLoadable
@Module(name = "ActiveModuleList", category = Category.CLIENT)
public class ActiveModuleList extends ModuleBase {

    Setting<String> listPos = setting("ListPos", "RightTop", listOf("RightTop", "RightDown", "LeftTop", "LeftDown"));

    @Listener
    public void onRender(RenderOverlayEvent event) {

        int startX = RenderHelper.getStart(event.getScaledResolution(),listPos.getValue()).x;
        int startY = RenderHelper.getStart(event.getScaledResolution(),listPos.getValue()).y;

        if (mc.player.getActivePotionEffects().size() > 0 && listPos.getValue().equals("RightTop")) {
            startY += 26;
        }

        int index = 0;

        List<ModuleBase> moduleList = Cursa.MODULE_BUS.getModules().stream()
                .sorted(Comparator.comparing(it -> -FontManager.getWidth(it.name))).collect(Collectors.toList());

        for (ModuleBase module : moduleList) {
            int color = GUIManager.isRainbow() ? rainbow(index * 100) : GUIManager.getColor3I();
            index++;
            switch (listPos.getValue()) {
                case "RightDown": {
                    FontManager.draw(module.name, startX - FontManager.getWidth(module.name), startY - FontManager.getHeight() * index, color);
                    break;
                }
                case "LeftTop": {
                    FontManager.draw(module.name, startX, startY + 3 + FontManager.getHeight() * (index - 1), color);
                    break;
                }
                case "LeftDown": {
                    FontManager.draw(module.name, startX, startY - FontManager.getHeight() * index, color);
                    break;
                }
                default: {
                    FontManager.draw(module.name, startX - FontManager.getWidth(module.name), startY + 3 + FontManager.getHeight() * (index - 1), color);
                    break;
                }
            }
        }
    }

    public int rainbow(int delay) {
        double rainbowState = Math.ceil((System.currentTimeMillis() + delay) / 20.0);
        rainbowState %= 360;
        return Color.getHSBColor((float) (rainbowState / 360.0f), 1.0f, 1.0f).getRGB();
    }

}
