package club.eridani.cursa.mixin.mixins.network;

import club.eridani.cursa.Cursa;
import club.eridani.cursa.event.decentraliized.DecentralizedPacketEvent;
import club.eridani.cursa.event.events.network.PacketEvent;
import io.netty.channel.ChannelHandlerContext;
import net.minecraft.client.Minecraft;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = NetworkManager.class, priority = 312312)
public class MixinNetWork {
    @Inject(method = "channelRead0", at = @At("HEAD"), cancellable = true)
    private void packetReceived(ChannelHandlerContext context, Packet<?> packet, CallbackInfo callbackInfo) {
        if (Minecraft.getMinecraft().player != null && Minecraft.getMinecraft().world != null) {
            final PacketEvent.Receive event = new PacketEvent.Receive(packet);
            DecentralizedPacketEvent.Receive.instance.post(event);
            Cursa.EVENT_BUS.post(event);
            if (event.isCancelled() && callbackInfo.isCancellable()) {
                callbackInfo.cancel();
            }
        }
    }

    @Inject(method = "sendPacket(Lnet/minecraft/network/Packet;)V", at = @At("HEAD"), cancellable = true)
    private void sendPacket(Packet<?> packetIn, CallbackInfo callbackInfo) {
        if (Minecraft.getMinecraft().player != null && Minecraft.getMinecraft().world != null) {
            final PacketEvent.Send event = new PacketEvent.Send(packetIn);
            DecentralizedPacketEvent.Send.instance.post(event);
            Cursa.EVENT_BUS.post(event);
            if (event.isCancelled() && callbackInfo.isCancellable()) {
                callbackInfo.cancel();
            }
        }
    }
}
