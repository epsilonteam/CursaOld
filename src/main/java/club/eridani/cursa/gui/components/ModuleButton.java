package club.eridani.cursa.gui.components;

import club.eridani.cursa.client.FontManager;
import club.eridani.cursa.client.GUIManager;
import club.eridani.cursa.gui.Component;
import club.eridani.cursa.gui.Panel;
import club.eridani.cursa.module.ModuleBase;
import club.eridani.cursa.setting.NumberSetting;
import club.eridani.cursa.setting.Setting;
import club.eridani.cursa.setting.settings.*;
import club.eridani.cursa.utils.SoundUtil;
import club.eridani.cursa.utils.Timer;
import net.minecraft.client.gui.Gui;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class ModuleButton extends club.eridani.cursa.gui.Component {

    public List<club.eridani.cursa.gui.Component> settings = new ArrayList<>();
    ModuleBase module;
    public Timer buttonTimer = new Timer();

    public ModuleButton(ModuleBase module, int width, int height, Panel father) {
        this.module = module;
        this.width = width;
        this.height = height;
        this.father = father;
        setup();
    }

    public void setup() {
        for (Setting<?> value : module.getSettings()) {
            if (value instanceof BooleanSetting)
                settings.add(new BooleanButton((BooleanSetting) value, width, height, father));
            if (value instanceof IntSetting || value instanceof FloatSetting || value instanceof DoubleSetting)
                settings.add(new NumberSlider((NumberSetting<?>) value, width, height, father));
            if (value instanceof ModeSetting) settings.add(new ModeButton((ModeSetting) value, width, height, father));
        }
        settings.add(new BindButton(module, width, height, father));
    }

    @Override
    public void render(int mouseX, int mouseY, float partialTicks) {

        int color = GUIManager.getColor3I();

        if (isHovered(mouseX, mouseY)) {
            color = (color & 0x7F7F7F) << 1;
        }

        Gui.drawRect(x, y - 1, x + width, y + height + 1, 0x85000000);
        font.drawString(module.name, x + 1, (int) (y + height / 2 - font.getHeight() / 2f) + 2, module.isEnabled() ? color : fontColor);
        FontManager.drawIcon(x + width - 2 - FontManager.getIconWidth(), y + 4, new Color(230, 230, 230, 230));
    }

    @Override
    public boolean mouseClicked(int mouseX, int mouseY, int mouseButton) {
        if (!isHovered(mouseX, mouseY))
            return false;
        if (mouseButton == 0) {
            module.toggle();
            SoundUtil.playButtonClick();
        } else if (mouseButton == 1) {
            buttonTimer.reset();
            isExtended = !isExtended;
            SoundUtil.playButtonClick();
        }
        return true;
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY, int state) {
        for (club.eridani.cursa.gui.Component setting : settings) {
            setting.mouseReleased(mouseX, mouseY, state);
        }
    }

    @Override
    public void keyTyped(char typedChar, int keyCode) {
        for (Component setting : settings) {
            setting.keyTyped(typedChar, keyCode);
        }
    }

}
