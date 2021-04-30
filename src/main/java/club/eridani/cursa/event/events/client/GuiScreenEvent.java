package club.eridani.cursa.event.events.client;

import club.eridani.cursa.event.CursaEvent;
import net.minecraft.client.gui.GuiScreen;

public class GuiScreenEvent extends CursaEvent {

    private final GuiScreen screen;

    public GuiScreenEvent(GuiScreen screen) {
        this.screen = screen;
    }

    public GuiScreen getScreen() {
        return screen;
    }

    public static class Displayed extends GuiScreenEvent {
        public Displayed(GuiScreen screen) {
            super(screen);
        }
    }

    public static class Closed extends GuiScreenEvent {
        public Closed(GuiScreen screen) {
            super(screen);
        }
    }

}
