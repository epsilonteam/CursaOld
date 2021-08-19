package club.eridani.cursa.client;

import club.eridani.cursa.Cursa;
import club.eridani.cursa.command.CommandBase;
import club.eridani.cursa.concurrent.event.Listener;
import club.eridani.cursa.concurrent.event.Priority;
import club.eridani.cursa.event.events.client.ChatEvent;
import club.eridani.cursa.utils.ChatUtil;
import club.eridani.cursa.utils.ClassUtil;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

import static club.eridani.cursa.concurrent.TaskManager.launch;

public class CommandManager {

    public static String cmdPrefix = ".";
    public List<CommandBase> commands = new ArrayList<>();

    public static void init() {
        Cursa.log.info("Loading Command Manager");
        instance = new CommandManager();
        instance.commands.clear();
        instance.loadCommands();
        Cursa.EVENT_BUS.register(instance);
    }

    private void loadCommands() {
        Set<Class<? extends CommandBase>> classList = ClassUtil.findClasses(Cursa.class.getPackage().getName(), CommandBase.class);
        classList.stream().sorted(Comparator.comparing(Class::getSimpleName)).forEach(clazz -> {
            try {
                CommandBase command = clazz.newInstance();
                commands.add(command);
            } catch (Exception e) {
                e.printStackTrace();
                System.err.println("Couldn't initiate Command " + clazz.getSimpleName() + "! Err: " + e.getClass().getSimpleName() + ", message: " + e.getMessage());
            }
        });
    }

    @Listener(priority = Priority.HIGHEST)
    public void onChat(ChatEvent event) {
        if (event.getMessage().startsWith(cmdPrefix)) {
            launch(() -> runCommands(event.getMessage()));
            event.cancel();
        }
    }

    public void runCommands(String s) {
        String readString = s.trim().substring(cmdPrefix.length()).trim();
        boolean commandResolved = false;
        boolean hasArgs = readString.trim().contains(" ");
        String commandName = hasArgs ? readString.split(" ")[0] : readString.trim();
        String[] args = hasArgs ? readString.substring(commandName.length()).trim().split(" ") : new String[0];

        for (CommandBase command : commands) {
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
