package club.eridani.cursa.gui;

import club.eridani.cursa.client.FontManager;
import club.eridani.cursa.client.GUIManager;
import club.eridani.cursa.gui.components.ModuleButton;
import club.eridani.cursa.gui.font.CFontRenderer;
import club.eridani.cursa.client.ModuleManager;
import club.eridani.cursa.module.Category;
import club.eridani.cursa.module.CursaModule;
import net.minecraft.client.gui.Gui;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Panel {

    public int x, y, width, height;
    public Category category;

    public boolean extended;
    boolean dragging;

    int x2, y2;

    CFontRenderer font;

    public List<ModuleButton> elements = new ArrayList<>();

    public Panel(Category category, int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.extended = true;
        this.dragging = false;
        this.category = category;
        font = FontManager.fontRenderer;
        setup();
    }

    public void setup() {
        for (CursaModule m : ModuleManager.getModules()) {
            if (m.category == category) {
                elements.add(new ModuleButton(m, width, height, this));
            }
        }
    }

    public void drawScreen(int mouseX, int mouseY, float partialTicks) {

        if (this.dragging) {
            x = x2 + mouseX;
            y = y2 + mouseY;
        }

        int panelColor = 0x85000000;
        int color = GUIManager.getColor4I();

        Gui.drawRect(x, y, x + width, y + height, color);
        font.drawString(category.categoryName, x + (width / 2f - font.getStringWidth(category.categoryName) / 2f), y + height / 2f - font.getHeight() / 2f + 2, 0xffefefef);
        Gui.drawRect(x, y + height, x + width, y + height + 1, panelColor);

        if (this.extended && !elements.isEmpty()) {
            int startY = y + height + 2;
            for (ModuleButton button : elements) {
                button.solvePos();
                button.y = startY;
                button.render(mouseX, mouseY, partialTicks);
                int settingY = startY - 1;
                startY += height + 1;
                if (button.isExtended) {
                    for (Component component : button.settings) {
                        if(!component.isVisible()) continue;
                        component.solvePos();
                        component.y = startY;
                        component.render(mouseX, mouseY, partialTicks);
                        startY += height;
                    }
                    Gui.drawRect(x, settingY, x + 1, startY, color);
                }
                startY += 1;
            }
        }
    }


    public boolean mouseClicked(int mouseX, int mouseY, int mouseButton) {
        if (mouseButton == 0 && isHovered(mouseX, mouseY)) {
            x2 = this.x - mouseX;
            y2 = this.y - mouseY;
            dragging = true;
            Collections.swap(GUIManager.guiRenderer.panels, 0, GUIManager.guiRenderer.panels.indexOf(this));
            return true;
        }
        if (mouseButton == 1 && isHovered(mouseX, mouseY)) {
            extended = !extended;
            return true;
        }
        return false;
    }

    public void mouseReleased(int mouseX, int mouseY, int state) {
        if (state == 0) {
            this.dragging = false;
        }
        for (Component part : elements) {
            part.mouseReleased(mouseX, mouseY, state);
        }

    }

    public void keyTyped(char typedChar, int keyCode) {
        for (Component part : elements) {
            part.keyTyped(typedChar, keyCode);
        }
    }

    public boolean isHovered(int mouseX, int mouseY) {
        return mouseX >= Math.min(x, x + width) && mouseX <= Math.max(x, x + width) && mouseY >= Math.min(y, y + height) && mouseY <= Math.max(y, y + height);
    }
}
