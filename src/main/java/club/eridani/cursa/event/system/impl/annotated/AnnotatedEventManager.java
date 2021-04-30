package club.eridani.cursa.event.system.impl.annotated;

import club.eridani.cursa.event.system.EventManager;
import club.eridani.cursa.event.system.Listener;
import club.eridani.cursa.event.system.dispatch.EventDispatcher;
import club.eridani.cursa.event.system.handler.EventHandler;
import club.eridani.cursa.event.system.handler.scan.EventHandlerScanner;
import club.eridani.cursa.event.system.impl.annotated.dispatch.MethodEventDispatcher;
import club.eridani.cursa.event.system.impl.annotated.handler.scan.MethodHandlerScanner;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * An {@link EventManager} implementation that uses methods marked
 * with the {@link Listener}
 * annotation.
 *
 * @author Daniel
 * @since May 31, 2017
 */
public final class AnnotatedEventManager implements EventManager {
    /**
     * Listener scanner implementation used to find all listeners inside
     * of a specific listener.
     */
    private final EventHandlerScanner eventHandlerScanner;

    /**
     * A map that pairs a listener container with a provided dispatcher implementation.
     */
    private final Map<Object, EventDispatcher> listenerDispatchers;

    public AnnotatedEventManager() {
        this.eventHandlerScanner = new MethodHandlerScanner();
        this.listenerDispatchers = new ConcurrentHashMap<>();
    }

    @Override
    public <E> E post(final E event) {
        // Iterate the stored dispatchers and notify all listeners
        for (final EventDispatcher dispatcher : listenerDispatchers.values())
            dispatcher.dispatch(event);

        return event;
    }


    @Override
    public boolean isRegistered(final Object listener) {
        return listenerDispatchers.containsKey(listener);
    }

    @Override
    public boolean register(final Object listenerContainer) {
        // Check if we've already got this object registered
        if (listenerDispatchers.containsKey(listenerContainer))
            return false;

        // locate all handlers inside of the container
        final Map<Class<?>, Set<EventHandler>> eventHandlers =
                eventHandlerScanner.locate(listenerContainer);
        if (eventHandlers.isEmpty())
            return false;

        // create a new dispatcher for this specific listener
        return listenerDispatchers.put(listenerContainer,
                new MethodEventDispatcher(eventHandlers)) == null;
    }

    @Override
    public boolean unregister(final Object listenerContainer) {
        // Remove the given listener container from the dispatchers map
        return listenerDispatchers.remove(listenerContainer) != null;
    }
}
