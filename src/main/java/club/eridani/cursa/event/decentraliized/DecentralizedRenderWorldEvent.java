package club.eridani.cursa.event.decentraliized;

import club.eridani.cursa.concurrent.decentralization.DecentralizedEvent;
import club.eridani.cursa.event.events.render.RenderWorldEvent;

public class DecentralizedRenderWorldEvent extends DecentralizedEvent<RenderWorldEvent> {
    public static DecentralizedRenderWorldEvent instance = new DecentralizedRenderWorldEvent();
}
