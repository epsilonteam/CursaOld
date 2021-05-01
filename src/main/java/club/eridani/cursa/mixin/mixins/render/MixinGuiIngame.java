package club.eridani.cursa.mixin.mixins.render;

import net.minecraft.client.gui.GuiIngame;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(GuiIngame.class)
public class MixinGuiIngame {
    /*
    @Inject(method = "renderGameOverlay", at = @At(value = "RETURN"))
    public void renderGameOverlay(float partialTicks, CallbackInfo ci) {
        Cursa.EVENT_BUS.post(new RenderOverlayEvent(partialTicks));
    }
     */
}
