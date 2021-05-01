package club.eridani.cursa.event.events.render;

import club.eridani.cursa.event.CursaEvent;

public class RenderModelEvent extends CursaEvent {
    public boolean rotating = false;
    public float pitch = 0;

    public RenderModelEvent(){
        super();
    }
}
