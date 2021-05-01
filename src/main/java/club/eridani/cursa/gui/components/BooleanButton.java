package club.eridani.cursa.gui.components;

import club.eridani.cursa.client.GUIManager;
import club.eridani.cursa.gui.Component;
import club.eridani.cursa.gui.Panel;
import club.eridani.cursa.setting.Setting;
import club.eridani.cursa.utils.SoundUtil;
import net.minecraft.client.gui.Gui;

public class BooleanButton extends Component {

    Setting<Boolean> setting;

    public BooleanButton(Setting<Boolean> setting, int width, int height, Panel father) {
        this.width = width;
        this.height = height;
        this.father = father;
        this.setting = setting;
    }

    @Override
    public void render(int mouseX, int mouseY, float partialTicks) {

        int color = GUIManager.getColor3I();
        Gui.drawRect(x, y, x + width, y + height, 0x85000000);

        int c = (setting.getValue() ? color : fontColor);
        if (isHovered(mouseX, mouseY)){
            c = (c & 0x7F7F7F) << 1;
        }

        font.drawString(setting.getName(), x + 3, (int) (y + height / 2 - font.getHeight() / 2f) + 2, c);

    }

    @Override
    public boolean mouseClicked(int mouseX, int mouseY, int mouseButton) {
        if (!setting.isVisible() || !isHovered(mouseX, mouseY))
            return false;
        if (mouseButton == 0) {
            this.setting.setValue(!setting.getValue());
            SoundUtil.playButtonClick();
        }
        return true;
    }

    @Override
    public boolean isVisible(){
        return setting.isVisible();
    }

}
