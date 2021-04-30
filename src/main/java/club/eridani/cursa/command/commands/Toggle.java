package club.eridani.cursa.command.commands;

import club.eridani.cursa.client.ModuleManager;
import club.eridani.cursa.command.Command;
import club.eridani.cursa.command.CursaCommand;
import club.eridani.cursa.utils.ChatUtil;

import java.util.Objects;

/**
 * Created by killRED on 2020
 * Updated by B_312 on 01/15/21
 */
@Command(command = "toggle",description = "Toggle selected module or HUD.")
public class Toggle extends CursaCommand {

    @Override
    public void onCall(String s, String[] args) {
        try {
            Objects.requireNonNull(ModuleManager.getModuleByName(args[0])).toggle();
        } catch(Exception e) {
            ChatUtil.sendNoSpamErrorMessage(getSyntax());
        }
    }

    @Override
    public String getSyntax() {
        return "toggle <modulename>";
    }

}