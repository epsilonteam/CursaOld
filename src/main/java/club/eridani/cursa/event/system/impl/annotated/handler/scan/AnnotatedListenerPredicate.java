package club.eridani.cursa.event.system.impl.annotated.handler.scan;

import club.eridani.cursa.event.system.Listener;

import java.lang.reflect.Method;
import java.util.function.Predicate;

public final class AnnotatedListenerPredicate implements Predicate<Method> {

    @Override
    public boolean test(final Method method) {
        return method.isAnnotationPresent(Listener.class) &&
                method.getParameterCount() == 1;
    }
}
