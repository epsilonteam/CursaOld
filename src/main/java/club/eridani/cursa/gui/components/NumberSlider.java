package club.eridani.cursa.gui.components;

import club.eridani.cursa.client.GUIManager;
import club.eridani.cursa.gui.Component;
import club.eridani.cursa.gui.Panel;
import club.eridani.cursa.setting.NumberSetting;
import club.eridani.cursa.setting.settings.DoubleSetting;
import club.eridani.cursa.setting.settings.FloatSetting;
import club.eridani.cursa.setting.settings.IntSetting;
import net.minecraft.client.gui.Gui;
import net.minecraft.util.math.MathHelper;

public class NumberSlider extends Component {

    boolean sliding = false;
    NumberSetting<?> setting;

    public NumberSlider(NumberSetting<?> setting, int width, int height, Panel father) {
        this.width = width;
        this.height = height;
        this.father = father;
        this.setting = setting;
    }

    @Override
    public void render(int mouseX, int mouseY, float partialTicks) {
        if (!setting.isVisible())
            sliding = false;

        int color = GUIManager.getColor4I();

        Gui.drawRect(x, y, x + width, y + height, 0x85000000);

        String displayValue = String.format("%.1f", setting.getValue().doubleValue());
        double percentBar = (setting.getValue().doubleValue() - setting.getMin().doubleValue()) / (setting.getMax().doubleValue() - setting.getMin().doubleValue());
        double tempWidth = (width - 2) * percentBar;

        Gui.drawRect(x + 1, y + 1, x + 1 + (int) tempWidth, y + height, color);

        if (this.sliding) {
            double diff = setting.getMax().doubleValue() - setting.getMin().doubleValue();
            double val = setting.getMin().doubleValue() + (MathHelper.clamp((mouseX - (double) (x + 1)) / (double) (width - 2), 0, 1)) * diff;
            if (setting instanceof DoubleSetting) {
                ((DoubleSetting) setting).setValue(val);
            } else if (setting instanceof FloatSetting) {
                ((FloatSetting) setting).setValue((float) val);
            } else if (setting instanceof IntSetting) {
                ((IntSetting) setting).setValue((int) val);
            }
        }

        font.drawString(setting.getName(), x + 3, (int) (y + height / 2 - font.getHeight() / 2f) + 2, fontColor);
        font.drawString(String.valueOf(displayValue), x + width - 1 - font.getStringWidth(String.valueOf(displayValue)), (int) (y + height / 2 - font.getHeight() / 2f) + 2, isHovered(mouseX, mouseY) ? fontColor : 0x909090);
    }

    @Override
    public boolean mouseClicked(int mouseX, int mouseY, int mouseButton) {
        if (!setting.isVisible() || !isHovered(mouseX, mouseY))
            return false;
        if (mouseButton == 0) {
            this.sliding = true;
            return true;
        }
        return false;
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY, int state) {
        sliding = false;
    }

    @Override
    public boolean isVisible() {
        return setting.isVisible();
    }

}
