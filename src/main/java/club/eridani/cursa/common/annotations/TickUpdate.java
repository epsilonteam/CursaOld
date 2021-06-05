package club.eridani.cursa.common.annotations;

import club.eridani.cursa.common.types.Tick;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface TickUpdate {
    Tick type() default Tick.Client;
}
