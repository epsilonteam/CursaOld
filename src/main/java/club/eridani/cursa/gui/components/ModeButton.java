package club.eridani.cursa.gui.components;

import club.eridani.cursa.gui.Component;
import club.eridani.cursa.gui.Panel;
import club.eridani.cursa.setting.Setting;
import club.eridani.cursa.setting.settings.ModeSetting;
import club.eridani.cursa.utils.ColorUtil;
import club.eridani.cursa.utils.SoundUtil;
import net.minecraft.client.gui.Gui;

import java.awt.*;

public class ModeButton extends Component {

    ModeSetting setting;

    public ModeButton(Setting<String> setting, int width, int height, Panel father) {
        this.width = width;
        this.height = height;
        this.father = father;
        this.setting = (ModeSetting) setting;
    }

    @Override
    public void render(int mouseX, int mouseY, float partialTicks) {

        Gui.drawRect(x, y, x + width, y + height, 0x85000000);
        font.drawString(setting.getName(), x + 3, (int) (y + height / 2 - font.getHeight() / 2f) + 2, ColorUtil.getHoovered(new Color(255, 255, 255).getRGB(), isHovered(mouseX, mouseY)));
        font.drawString(setting.getValue(),
                x + width - 1 - font.getStringWidth(setting.getValue()), (int) (y + height / 2 - font.getHeight() / 2f) + 2,
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

}
