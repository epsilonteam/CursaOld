package club.eridani.cursa.command.commands;


import club.eridani.cursa.client.CommandManager;
import club.eridani.cursa.command.CommandBase;
import club.eridani.cursa.common.annotations.Command;
import club.eridani.cursa.utils.ChatUtil;

/**
 * Created by B_312 on 01/15/21
 */
@Command(command = "commands", description = "Lists all commands.")
public class Commands extends CommandBase {

    @Override
    public void onCall(String s, String[] args) {
        ChatUtil.printChatMessage("\247b" + "Commands:");
        try {
            for (CommandBase cmd : CommandManager.getInstance().commands) {
                if (cmd == this) {
                    continue;
                }
                ChatUtil.printChatMessage("\247b" + cmd.getSyntax().replace("<", "\2473<\2479").replace(">", "\2473>") + "\2478" + " - " + cmd.getDescription());
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