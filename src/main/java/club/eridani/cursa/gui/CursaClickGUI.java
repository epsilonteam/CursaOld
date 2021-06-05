package club.eridani.cursa.gui;

import club.eridani.cursa.client.GUIManager;
import club.eridani.cursa.client.ModuleManager;
import club.eridani.cursa.module.modules.client.ClickGUI;
import club.eridani.cursa.utils.particles.ParticleSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.ResourceLocation;

import java.awt.*;

public class CursaClickGUI extends GuiScreen {

    private final ParticleSystem particleSystem = new ParticleSystem(100);

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }

    @Override
    public void initGui() {
        if (GUIManager.getBackground().equals(GUIManager.Background.Blur) || GUIManager.getBackground().equals(GUIManager.Background.Both)) {
            if (Minecraft.getMinecraft().entityRenderer.getShaderGroup() != null)
                Minecraft.getMinecraft().entityRenderer.getShaderGroup().deleteShaderGroup();
            Minecraft.getMinecraft().entityRenderer.loadShader(new ResourceLocation("shaders/post/blur.json"));
        }
    }


    @Override
    public void onGuiClosed() {
        if (Minecraft.getMinecraft().entityRenderer.getShaderGroup() != null)
            Minecraft.getMinecraft().entityRenderer.getShaderGroup().deleteShaderGroup();
        if (ModuleManager.getModule(ClickGUI.class).isEnabled()) {
            ModuleManager.getModule(ClickGUI.class).disable();
        }
    }


    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        if (GUIManager.getBackground().equals(GUIManager.Background.Shadow) || GUIManager.getBackground().equals(GUIManager.Background.Both)) {
            drawDefaultBackground();
        }
        if (mc.player == null) Gui.drawRect(0, 0, 9999, 9999, new Color(0, 0, 0, 255).getRGB());

        if (GUIManager.isParticle()) {
            particleSystem.tick(10);
            particleSystem.render();
        }

        GUIRenderer.instance.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        GUIRenderer.instance.mouseClicked(mouseX, mouseY, mouseButton);
    }

    @Override
    public void keyTyped(char typedChar, int keyCode) {
        GUIRenderer.instance.keyTyped(typedChar, keyCode);
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY, int state) {
        GUIRenderer.instance.mouseReleased(mouseX, mouseY, state);
    }
}
