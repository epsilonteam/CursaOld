package club.eridani.cursa.common.annotations;

import club.eridani.cursa.module.Category;
import org.lwjgl.input.Keyboard;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface Module {
    String name();
    int keyCode() default Keyboard.KEY_NONE;
    Category category();
    boolean visible() default true;
}
