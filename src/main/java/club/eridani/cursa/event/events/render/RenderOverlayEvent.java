package club.eridani.cursa.event.events.render;

import club.eridani.cursa.event.CursaEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;

public final class RenderOverlayEvent extends CursaEvent {

    private final float partialTicks;
    private final ScaledResolution scaledResolution;

    public RenderOverlayEvent(float partialTicks) {
        this.partialTicks = partialTicks;
        this.scaledResolution = new ScaledResolution(Minecraft.getMinecraft());
    }

    public final float getPartialTicks() {
        return partialTicks;
    }

    public final ScaledResolution getScaledResolution() {
        return this.scaledResolution;
    }

}
