package net.spartanb312.cursa.gui.components;

import net.spartanb312.cursa.gui.Panel;
import net.spartanb312.cursa.setting.Setting;
import net.spartanb312.cursa.setting.settings.ModeSetting;
import net.spartanb312.cursa.utils.ColorUtil;
import net.spartanb312.cursa.utils.SoundUtil;
import net.spartanb312.cursa.utils.graphics.RenderUtils2D;

import java.awt.*;

public class ModeButton extends net.spartanb312.cursa.gui.Component {

    ModeSetting setting;

    public ModeButton(Setting<String> setting, int width, int height, Panel father) {
        this.width = width;
        this.height = height;
        this.father = father;
        this.setting = (ModeSetting) setting;
    }

    @Override
    public void render(int mouseX, int mouseY, float partialTicks) {
        RenderUtils2D.drawRect(x, y, x + width, y + height, 0x85000000);
        font.drawString(setting.getName(), x + 5, (int) (y + height / 2 - font.getHeight() / 2f) + 2, ColorUtil.getHoovered(new Color(255, 255, 255).getRGB(), isHovered(mouseX, mouseY)));
        font.drawString(setting.getValue(),
                x + width - 3 - font.getStringWidth(setting.getValue()), (int) (y + height / 2 - font.getHeight() / 2f) + 2,
                isHovered(mouseX, mouseY) ? fontColor : 0x909090);
    }

    @Override
    public boolean mouseClicked(int mouseX, int mouseY, int mouseButton) {
        if (!isHovered(mouseX, mouseY) || !setting.isVisible()) return false;
        if (mouseButton == 0) {
            setting.forwardLoop();
            SoundUtil.playButtonClick();
        }
        return true;
    }

    @Override
    public boolean isVisible() {
        return setting.isVisible();
    }

    @Override
    public String getDescription() {
        return setting.getDescription();
    }

}
