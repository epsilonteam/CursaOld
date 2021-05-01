package club.eridani.cursa.gui.components;

import club.eridani.cursa.client.GUIManager;
import club.eridani.cursa.gui.Component;
import club.eridani.cursa.gui.Panel;
import club.eridani.cursa.client.ModuleManager;
import club.eridani.cursa.module.ModuleBase;
import club.eridani.cursa.module.modules.client.ClickGUI;
import club.eridani.cursa.utils.SoundUtil;
import net.minecraft.client.gui.Gui;
import org.lwjgl.input.Keyboard;

public class BindButton extends Component {

    ModuleBase module;
    boolean accepting = false;

    public BindButton(ModuleBase module, int width, int height, Panel father) {
        this.module = module;
        this.width = width;
        this.height = height;
        this.father = father;
    }

    @Override
    public void render(int mouseX, int mouseY, float partialTicks) {

        int color = GUIManager.getColor3I();
        Gui.drawRect(x, y, x + width, y + height, 0x85000000);

        int c = (accepting ? color : fontColor);
        if (isHovered(mouseX, mouseY)) {
            c = (c & 0x7F7F7F) << 1;
        }

        font.drawString(accepting ? "Bind | ..." : "Bind | " + (module.keyCode == 0x00 ? "NONE" : Keyboard.getKeyName(module.keyCode)), x + 3, (int) (y + height / 2 - font.getHeight() / 2f) + 2, c);
    }

    @Override
    public void keyTyped(char typedChar, int keyCode) {
        super.keyTyped(typedChar, keyCode);
        if (accepting) {
            if (keyCode == ModuleManager.getModule(ClickGUI.class).keyCode) {
                module.keyCode = Keyboard.KEY_NONE;
            } else {
                module.keyCode = keyCode;
            }
            accepting = false;
        }
    }

    @Override
    public boolean mouseClicked(int mouseX, int mouseY, int mouseButton) {
        if (!isHovered(mouseX, mouseY))
            return false;

        if (mouseButton == 0) {
            accepting = true;
            SoundUtil.playButtonClick();
        } else if (mouseButton == 1) {
            this.module.keyCode = Keyboard.KEY_NONE;
            SoundUtil.playAnvilHit();
        }
        return true;

    }

}
