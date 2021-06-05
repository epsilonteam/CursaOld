package club.eridani.cursa.command;

import club.eridani.cursa.common.annotations.Command;
import net.minecraft.client.Minecraft;

public abstract class CommandBase {

    private final String command;
    private final String description;

    public static final Minecraft mc = Minecraft.getMinecraft();

    public CommandBase() {
        this.command = getAnnotation().command();
        this.description = getAnnotation().description();
    }

    private Command getAnnotation() {
        if (getClass().isAnnotationPresent(Command.class)) {
            return getClass().getAnnotation(Command.class);
        }
        throw new IllegalStateException("No Annotation on class " + this.getClass().getCanonicalName() + "!");
    }

    public abstract void onCall(String s, String... args);

    public abstract String getSyntax();

    public String getDescription() {
        return description;
    }

    public String getCommand() {
        return command;
    }
}