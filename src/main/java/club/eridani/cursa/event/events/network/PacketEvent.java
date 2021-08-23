package club.eridani.cursa.event.events.network;

import club.eridani.cursa.concurrent.decentralization.EventData;
import club.eridani.cursa.event.CursaEvent;
import net.minecraft.network.Packet;

public class PacketEvent extends CursaEvent implements EventData {

    public final Packet<?> packet;

    public PacketEvent(final Packet<?> packet) {
        this.packet = packet;
    }

    public static class Receive extends PacketEvent {
        public Receive(final Packet<?> packet) {
            super(packet);
        }
    }

    public static class Send extends PacketEvent {
        public Send(final Packet<?> packet) {
            super(packet);
        }
    }

    public Packet<?> getPacket() {
        return packet;
    }
}
