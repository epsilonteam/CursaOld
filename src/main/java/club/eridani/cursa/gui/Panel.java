package club.eridani.cursa.gui;

import club.eridani.cursa.client.FontManager;
import club.eridani.cursa.client.GUIManager;
import club.eridani.cursa.client.ModuleManager;
import club.eridani.cursa.gui.components.ModuleButton;
import club.eridani.cursa.gui.font.CFontRenderer;
import club.eridani.cursa.module.Category;
import club.eridani.cursa.module.ModuleBase;
import club.eridani.cursa.utils.Timer;
import net.minecraft.client.gui.Gui;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

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

    Timer panelTimer = new Timer();

    public void setup() {
        for (ModuleBase m : ModuleManager.getModules()) {
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

        if (!elements.isEmpty()) {
            int startY = y + height + 2;
            int index = 0;
            for (ModuleButton button : elements) {
                index++;
                if (extended) {
                    if (!panelTimer.passed(index * 25)) continue;
                } else {
                    if (panelTimer.passed((elements.size() - index) * 25)) continue;
                }
                button.solvePos();
                button.y = startY;
                button.render(mouseX, mouseY, partialTicks);
                int settingY = startY - 1;
                startY += height + 1;

                int extendedCount = 0;
                int settingIndex = -1;
                List<Component> visibleSettings = button.settings.stream().filter(Component::isVisible).collect(Collectors.toList());

                for (Component component : visibleSettings) {
                    settingIndex++;
                    if (button.isExtended) {
                        if (!button.buttonTimer.passed(settingIndex * 25)) continue;
                    } else {
                        if (button.buttonTimer.passed((visibleSettings.size() - settingIndex) * 25)) continue;
                    }
                    extendedCount++;
                    component.solvePos();
                    component.y = startY;
                    component.render(mouseX, mouseY, partialTicks);
                    startY += height;
                }
                if (extendedCount != 0) {
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
            Collections.swap(GUIRenderer.instance.panels, 0, GUIRenderer.instance.panels.indexOf(this));
            return true;
        }
        if (mouseButton == 1 && isHovered(mouseX, mouseY)) {
            extended = !extended;
            panelTimer.reset();
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
