package club.eridani.cursa.mixin.mixins.render;

import club.eridani.cursa.Cursa;
import club.eridani.cursa.event.events.render.HudOverlayEvent;
import club.eridani.cursa.event.events.render.RenderWorldEvent;
import net.minecraft.client.renderer.EntityRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EntityRenderer.class)
public class MixinEntityRenderer {

    /**
     * Mixin have bugs,sometimes we may inject failed,so we use ASM
     * @club.eridani.cursa.asm.impl.PatchEntityRenderer
     */
    @Inject(method = "updateCameraAndRender", at = @At(value = "INVOKE", target = "net/minecraft/client/gui/GuiIngame.renderGameOverlay(F)V"))
    public void updateCameraAndRender$renderGameOverlay(float partialTicks, long nanoTime, CallbackInfo ci) {
        //Cursa.EVENT_BUS.post(new RenderOverlayEvent(partialTicks));
    }


    @Inject(method = "hurtCameraEffect", at = @At("HEAD"), cancellable = true)
    public void hurtCameraEffect(float partialTicks, CallbackInfo ci) {
        HudOverlayEvent event = new HudOverlayEvent(HudOverlayEvent.Type.HURTCAM);
        Cursa.EVENT_BUS.post(event);
        if (event.isCancelled())
            ci.cancel();
    }

    @Inject(method = "renderWorldPass", at = @At(value = "INVOKE_STRING", target = "net/minecraft/profiler/Profiler.endStartSection(Ljava/lang/String;)V", args = "ldc=hand"))
    public void onStartHand(int pass, float partialTicks, long finishTimeNano, CallbackInfo ci) {
        Cursa.EVENT_BUS.post(new RenderWorldEvent(partialTicks, pass));
    }

}
