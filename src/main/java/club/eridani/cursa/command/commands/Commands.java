package club.eridani.cursa.command.commands;


import club.eridani.cursa.client.CommandManager;
import club.eridani.cursa.command.Command;
import club.eridani.cursa.command.CursaCommand;
import club.eridani.cursa.utils.ChatUtil;

/**
 * Created by B_312 on 01/15/21
 */
@Command(command = "commands", description = "Lists all commands.")
public class Commands extends CursaCommand {

    @Override
    public void onCall(String s, String[] args) {
        ChatUtil.printChatMessage("\247a" + "Commands:");
        try {
            for (CursaCommand cmd : CommandManager.getInstance().commands) {
                if (cmd == this) {
                    continue;
                }
                ChatUtil.printChatMessage("\247a" + cmd.getSyntax().replace("<", "\247e<\2476").replace(">", "\247e>") + "\2477" + " - " + cmd.getDescription());
            }
        } catch (Exception e) {
            ChatUtil.sendNoSpamErrorMessage(getSyntax());
        }
    }

    @Override
    public String getSyntax() {
        return "commands";
    }

}