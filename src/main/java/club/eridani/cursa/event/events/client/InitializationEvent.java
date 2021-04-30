package club.eridani.cursa.event.events.client;

import club.eridani.cursa.event.CursaEvent;

/**
 * We use this to launch our client
 */
public class InitializationEvent extends CursaEvent {
    public static class PreInitialize extends InitializationEvent{
    }

    public static class Initialize extends InitializationEvent{
    }

    public static class PostInitialize extends InitializationEvent{
    }
}
