package club.eridani.cursa.client;

import club.eridani.cursa.Cursa;
import club.eridani.cursa.command.CursaCommand;
import club.eridani.cursa.event.events.client.ChatEvent;
import club.eridani.cursa.event.system.Listener;
import club.eridani.cursa.utils.ChatUtil;
import club.eridani.cursa.utils.ClassUtil;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

public class CommandManager {

    public static String cmdPrefix = ".";
    public List<CursaCommand> commands = new ArrayList<>();

    public static void init() {
        if (instance == null) instance = new CommandManager();
        instance.commands.clear();
        instance.loadCommands();
        Cursa.EVENT_BUS.register(instance);
    }

    private void loadCommands() {
        Set<Class<? extends CursaCommand>> classList = ClassUtil.findClasses(CursaCommand.class.getPackage().getName(), CursaCommand.class);
        classList.stream().sorted(Comparator.comparing(Class::getSimpleName)).forEach(clazz -> {
            try {
                CursaCommand command = clazz.newInstance();
                commands.add(command);
            } catch (Exception e) {
                e.printStackTrace();
                System.err.println("Couldn't initiate Command " + clazz.getSimpleName() + "! Err: " + e.getClass().getSimpleName() + ", message: " + e.getMessage());
            }
        });
    }

    @Listener
    public void onChat(ChatEvent event) {
        if (event.getMessage().startsWith(cmdPrefix)) {
            runCommands(event.getMessage());
            event.cancel();
        }
    }

    public void runCommands(String s) {
        String readString = s.trim().substring(cmdPrefix.length()).trim();
        boolean commandResolved = false;
        boolean hasArgs = readString.trim().contains(" ");
        String commandName = hasArgs ? readString.split(" ")[0] : readString.trim();
        String[] args = hasArgs ? readString.substring(commandName.length()).trim().split(" ") : new String[0];

        for (CursaCommand command : commands) {
            if (command.getCommand().trim().equalsIgnoreCase(commandName.trim().toLowerCase())) {
                command.onCall(readString, args);
                commandResolved = true;
                break;
            }
        }
        if (!commandResolved) {
            ChatUtil.sendNoSpamErrorMessage("Unknown command. try 'help' for a list of commands.");
        }
    }

    private static CommandManager instance;

    public static CommandManager getInstance() {
        if (instance == null) instance = new CommandManager();
        return instance;
    }

}
