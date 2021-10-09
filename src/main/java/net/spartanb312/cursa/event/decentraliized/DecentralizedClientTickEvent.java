package net.spartanb312.cursa.event.decentraliized;

import net.spartanb312.cursa.concurrent.decentralization.DecentralizedEvent;
import net.spartanb312.cursa.concurrent.decentralization.EventData;

public class DecentralizedClientTickEvent extends DecentralizedEvent<EventData> {
    public static DecentralizedClientTickEvent instance = new DecentralizedClientTickEvent();
}
