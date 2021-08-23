package club.eridani.cursa.concurrent.event;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface Listener {
    int priority() default Priority.Medium;
}
