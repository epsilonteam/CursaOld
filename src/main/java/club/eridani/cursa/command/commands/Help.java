package club.eridani.cursa.command.commands;

import club.eridani.cursa.Cursa;
import club.eridani.cursa.client.CommandManager;
import club.eridani.cursa.client.ModuleManager;
import club.eridani.cursa.command.Command;
import club.eridani.cursa.command.CursaCommand;
import club.eridani.cursa.module.modules.client.ClickGUI;
import club.eridani.cursa.utils.ChatUtil;
import org.lwjgl.input.Keyboard;

/**
 * Created by B_312 on 01/15/21
 */
@Command(command = "help", description = "Get helps.")
public class Help extends CursaCommand {

    @Override
    public void onCall(String s, String[] args) {
        ChatUtil.printChatMessage("\2476" + Cursa.MOD_NAME + " " + "\247a" + Cursa.MOD_VERSION);
        ChatUtil.printChatMessage("\247c" + "Made by: " + Cursa.AUTHOR);
        ChatUtil.printChatMessage("\247c" + "Github: " + Cursa.GITHUB);
        ChatUtil.printChatMessage("\247a" + "Press " + "\247c" + Keyboard.getKeyName(ModuleManager.getModule(ClickGUI.class).keyCode) + "\247a" + " to open ClickGUI");
        ChatUtil.printChatMessage("\247a" + "Use command: " + "\247e" + CommandManager.cmdPrefix + "prefix <target prefix>" + "\247a" + " to set command prefix");
        ChatUtil.printChatMessage("\247a" + "List all available commands: " + "\247e" + CommandManager.cmdPrefix + "commands");
    }

    @Override
    public String getSyntax() {
        return "help";
    }

}