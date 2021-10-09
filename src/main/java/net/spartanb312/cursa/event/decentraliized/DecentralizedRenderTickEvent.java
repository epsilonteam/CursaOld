package net.spartanb312.cursa.event.decentraliized;

import net.spartanb312.cursa.concurrent.decentralization.DecentralizedEvent;
import net.spartanb312.cursa.event.events.render.RenderOverlayEvent;

public class DecentralizedRenderTickEvent extends DecentralizedEvent<RenderOverlayEvent> {
    public static DecentralizedRenderTickEvent instance = new DecentralizedRenderTickEvent();
}
