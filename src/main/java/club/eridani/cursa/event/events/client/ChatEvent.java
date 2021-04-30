package club.eridani.cursa.event.events.client;

import club.eridani.cursa.event.CursaEvent;

public class ChatEvent extends CursaEvent {

    protected String message;

    public ChatEvent(String message) {
        this.message = message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public final String getMessage() {
        return this.message;
    }

}