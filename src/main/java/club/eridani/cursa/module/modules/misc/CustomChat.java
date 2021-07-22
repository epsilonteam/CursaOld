package club.eridani.cursa.module.modules.misc;

import club.eridani.cursa.Cursa;
import club.eridani.cursa.common.annotations.Module;
import club.eridani.cursa.common.annotations.ParallelLoadable;
import club.eridani.cursa.event.events.network.PacketEvent;
import club.eridani.cursa.module.Category;
import club.eridani.cursa.module.ModuleBase;
import club.eridani.cursa.setting.Setting;
import net.minecraft.network.play.client.CPacketChatMessage;

@ParallelLoadable
@Module(name = "CustomChat", category = Category.MISC)
public class CustomChat extends ModuleBase {

    Setting<Boolean> commands = setting("Commands", false);

    @Override
    public void onPacketSend(PacketEvent.Send event) {
        if (event.packet instanceof CPacketChatMessage) {
            String s = ((CPacketChatMessage) event.getPacket()).getMessage();
            if (s.startsWith("/") && !commands.getValue()) return;
            s += Cursa.CHAT_SUFFIX;
            if (s.length() >= 256) s = s.substring(0, 256);
            ((CPacketChatMessage) event.getPacket()).message = s;
        }
    }

}
