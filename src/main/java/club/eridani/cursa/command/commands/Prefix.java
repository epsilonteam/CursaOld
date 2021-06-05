package club.eridani.cursa.command.commands;


import club.eridani.cursa.client.CommandManager;
import club.eridani.cursa.command.CommandBase;
import club.eridani.cursa.common.annotations.Command;
import club.eridani.cursa.utils.ChatUtil;
import club.eridani.cursa.utils.SoundUtil;

/**
 * Created by killRED on 2020
 * Updated by B_312 on 01/15/21
 */
@Command(command = "prefix", description = "Set command prefix.")
public class Prefix extends CommandBase {

    @Override
    public void onCall(String s, String[] args) {
        if (args.length <= 0) {
            ChatUtil.sendNoSpamErrorMessage("Please specify a new prefix!");
            return;
        }
        if (args[0] != null) {
            CommandManager.cmdPrefix = args[0];
            ChatUtil.sendNoSpamMessage("Prefix set to " + ChatUtil.SECTIONSIGN + "b" + args[0] + "!");
            SoundUtil.playButtonClick();
        }
    }

    @Override
    public String getSyntax() {
        return "prefix <char>";
    }

}
