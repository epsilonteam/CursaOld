package club.eridani.cursa.gui;

import club.eridani.cursa.client.FontManager;
import club.eridani.cursa.gui.font.CFontRenderer;
import net.minecraft.client.Minecraft;

import java.awt.*;

/**
 * Created by B_312 on 01/10/21
 */
public abstract class Component {

    public CFontRenderer font = FontManager.fontRenderer;
    public Minecraft mc = Minecraft.getMinecraft();
    public int x,y,width,height;
    protected int fontColor = new Color(255, 255, 255).getRGB();
    public Panel father;
    public boolean isExtended;
    public abstract void render(int mouseX, int mouseY, float partialTicks);
    public abstract boolean mouseClicked(int mouseX, int mouseY, int mouseButton);
    public void mouseReleased(int mouseX, int mouseY, int state) { }
    public void keyTyped(char typedChar, int keyCode) { }

    public void solvePos(){
        this.x = father.x;
        this.y = father.y;
    }

    public boolean isHovered(int mouseX, int mouseY) {
        return mouseX >= Math.min(x, x + width) && mouseX <= Math.max(x, x + width) && mouseY >= Math.min(y, y + height) && mouseY <= Math.max(y, y + height);
    }

    public boolean isVisible(){
        return true;
    }

}
