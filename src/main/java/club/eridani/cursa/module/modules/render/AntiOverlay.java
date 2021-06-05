package club.eridani.cursa.module.modules.render;

import club.eridani.cursa.common.annotations.Module;
import club.eridani.cursa.common.annotations.PacketListener;
import club.eridani.cursa.common.annotations.ParallelLoadable;
import club.eridani.cursa.common.annotations.ParallelRunnable;
import club.eridani.cursa.common.types.IO;
import club.eridani.cursa.event.events.network.PacketEvent;
import club.eridani.cursa.module.Category;
import club.eridani.cursa.module.ModuleBase;
import club.eridani.cursa.setting.Setting;
import net.minecraft.init.MobEffects;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.SPacketExplosion;
import net.minecraft.network.play.server.SPacketSpawnExperienceOrb;
import net.minecraft.network.play.server.SPacketSpawnMob;
import net.minecraft.network.play.server.SPacketSpawnPainting;

@ParallelLoadable
@Module(name = "AntiOverlay", category = Category.RENDER)
public class AntiOverlay extends ModuleBase {

    Setting<Boolean> blindness = setting("Blindness", true);
    Setting<Boolean> nausea = setting("Nausea", false);
    Setting<Boolean> xp = setting("XP", false);
    Setting<Boolean> mob = setting("Mob", false);
    Setting<Boolean> explosion = setting("Explosions", true);
    Setting<Boolean> paint = setting("Paintings", false);

    @ParallelRunnable
    public void onParallelTick() {
        if (mc.player == null) return;
        if (blindness.getValue()) {
            mc.player.removeActivePotionEffect(MobEffects.BLINDNESS);
        }

        if (nausea.getValue()) {
            mc.player.removeActivePotionEffect(MobEffects.NAUSEA);
        }
    }

    @PacketListener(channel = IO.Receive)
    public void onPacketReceive(PacketEvent.Receive event) {
        Packet<?> packet = event.packet;
        if ((packet instanceof SPacketSpawnExperienceOrb && xp.getValue())
                || (packet instanceof SPacketExplosion && explosion.getValue())
                || (packet instanceof SPacketSpawnPainting && paint.getValue())
                || packet instanceof SPacketSpawnMob && mob.getValue()) {
            event.cancel();
        }
    }

}