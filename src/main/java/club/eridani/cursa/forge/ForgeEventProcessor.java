package club.eridani.cursa.forge;

import club.eridani.cursa.Cursa;
import net.minecraftforge.client.event.*;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.network.FMLNetworkEvent;

public class ForgeEventProcessor {

    private static ForgeEventProcessor instance;

    public static ForgeEventProcessor getInstance() {
        if (instance == null) instance = new ForgeEventProcessor();
        return instance;
    }

    @SubscribeEvent
    public void onRenderTick(TickEvent.RenderTickEvent event) {
        Cursa.EVENT_BUS.post(event);
    }

    @SubscribeEvent
    public void onRenderPre(RenderGameOverlayEvent.Pre event) {
        Cursa.EVENT_BUS.post(event);
    }

    @SubscribeEvent
    public void onRenderPost(RenderGameOverlayEvent.Post event) {
        Cursa.EVENT_BUS.post(event);
    }

    @SubscribeEvent
    public void onWorldRender(RenderWorldLastEvent event) {
        Cursa.EVENT_BUS.post(event);
    }

    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent event) {
        Cursa.EVENT_BUS.post(event);
    }

    @SubscribeEvent
    public void onKey(InputUpdateEvent event) {
        Cursa.EVENT_BUS.post(event);
    }

    @SubscribeEvent
    public void onKeyInput(InputEvent.KeyInputEvent event) {
        Cursa.EVENT_BUS.post(event);
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void onChatSend(ClientChatEvent event) {
        Cursa.EVENT_BUS.post(event);
    }

    @SubscribeEvent
    public void onAttack(AttackEntityEvent event) {
        Cursa.EVENT_BUS.post(event);
    }

    @SubscribeEvent
    public void onDeath(LivingDeathEvent event) {
        Cursa.EVENT_BUS.post(event);
    }

    @SubscribeEvent
    public void onLeftClick(PlayerInteractEvent.LeftClickBlock event) {
        Cursa.EVENT_BUS.post(event);
    }

    @SubscribeEvent
    public void onMouseInput(InputEvent.MouseInputEvent event) {
        Cursa.EVENT_BUS.post(event);
    }

    @SubscribeEvent
    public void onDisconnect(FMLNetworkEvent.ClientDisconnectionFromServerEvent event) {
        Cursa.EVENT_BUS.post(event);
    }

    @SubscribeEvent
    public void onPush(PlayerSPPushOutOfBlocksEvent event) {
        Cursa.EVENT_BUS.post(event);
    }

    @SubscribeEvent
    public void onConnect(FMLNetworkEvent.ClientConnectedToServerEvent event) {
        Cursa.EVENT_BUS.post(event);
    }

    @SubscribeEvent
    public void onRenderBlockOverlay(RenderBlockOverlayEvent event) {
        Cursa.EVENT_BUS.post(event);
    }

    @SubscribeEvent
    public void onRenderPlayerPre(RenderPlayerEvent.Pre event) {
        Cursa.EVENT_BUS.post(event);
    }

    @SubscribeEvent
    public void onDrawHighLight(DrawBlockHighlightEvent event) {
        Cursa.EVENT_BUS.post(event);
    }

}
