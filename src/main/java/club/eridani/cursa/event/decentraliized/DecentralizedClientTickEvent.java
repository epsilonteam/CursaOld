package club.eridani.cursa.event.decentraliized;

import club.eridani.cursa.concurrent.decentralization.DecentralizedEvent;
import club.eridani.cursa.concurrent.decentralization.EventData;

public class DecentralizedClientTickEvent extends DecentralizedEvent<EventData> {
    public static DecentralizedClientTickEvent instance = new DecentralizedClientTickEvent();
    public int stage = 0;
}
