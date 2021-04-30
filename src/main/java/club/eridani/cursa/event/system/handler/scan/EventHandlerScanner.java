package club.eridani.cursa.event.system.handler.scan;

import club.eridani.cursa.event.system.handler.EventHandler;

import java.util.Map;
import java.util.Set;

/**
 * Attempts to locate all event listeners in a given object and
 * stores them in a unmodifiable list. ({@see #getImmutableListeners})
 *
 * @author Daniel
 * @since May 31, 2017
 */
public interface EventHandlerScanner {

    /**
     * Check the given object for any possible listeners that are
     * contained inside.
     *
     * @return true if listeners located, false otherwise
     */
    Map<Class<?>, Set<EventHandler>> locate(Object listenerContainer);
}
