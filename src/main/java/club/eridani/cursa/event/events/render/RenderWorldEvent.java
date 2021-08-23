package club.eridani.cursa.event.events.render;

import club.eridani.cursa.concurrent.decentralization.EventData;
import club.eridani.cursa.event.CursaEvent;

public final class RenderWorldEvent extends CursaEvent implements EventData {

    private final float partialTicks;
    private final Pass pass;

    public RenderWorldEvent(float partialTicks, int pass) {
        this.partialTicks = partialTicks;
        this.pass = Pass.values()[pass];
    }

    public final Pass getPass() {
        return this.pass;
    }

    public final float getPartialTicks() {
        return partialTicks;
    }

    public enum Pass {
        ANAGLYPH_CYAN, ANAGLYPH_RED, NORMAL
    }

}
