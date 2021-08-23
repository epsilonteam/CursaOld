package club.eridani.cursa.event.decentraliized;

import club.eridani.cursa.concurrent.decentralization.DecentralizedEvent;
import club.eridani.cursa.event.events.render.RenderOverlayEvent;

public class DecentralizedRenderTickEvent extends DecentralizedEvent<RenderOverlayEvent> {
    public static DecentralizedRenderTickEvent instance = new DecentralizedRenderTickEvent();
    public int stage = 0;
}
