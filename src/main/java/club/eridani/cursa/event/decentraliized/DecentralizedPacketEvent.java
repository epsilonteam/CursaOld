package club.eridani.cursa.event.decentraliized;

import club.eridani.cursa.concurrent.decentralization.DecentralizedEvent;

public class DecentralizedPacketEvent {
    public static class Send extends DecentralizedEvent<club.eridani.cursa.event.events.network.PacketEvent.Send> {
        public static Send instance = new Send();
    }

    public static class Receive extends DecentralizedEvent<club.eridani.cursa.event.events.network.PacketEvent.Receive> {
        public static Receive instance = new Receive();
    }
}
