package club.eridani.cursa.command.commands;

import club.eridani.cursa.Cursa;
import club.eridani.cursa.client.CommandManager;
import club.eridani.cursa.client.ModuleManager;
import club.eridani.cursa.command.CommandBase;
import club.eridani.cursa.common.annotations.Command;
import club.eridani.cursa.module.modules.client.ClickGUI;
import club.eridani.cursa.utils.ChatUtil;
import org.lwjgl.input.Keyboard;

/**
 * Created by B_312 on 01/15/21
 */
@Command(command = "help", description = "Get helps.")
public class Help extends CommandBase {

    @Override
    public void onCall(String s, String[] args) {
        ChatUtil.printChatMessage("\247b" + Cursa.MOD_NAME + " " + "\247a" + Cursa.MOD_VERSION);
        ChatUtil.printChatMessage("\247c" + "Made by: " + Cursa.AUTHOR);
        ChatUtil.printChatMessage("\247c" + "Github: " + Cursa.GITHUB);
        ChatUtil.printChatMessage("\2473" + "Press " + "\247c" + Keyboard.getKeyName(ModuleManager.getModule(ClickGUI.class).keyCode) + "\2473" + " to open ClickGUI");
        ChatUtil.printChatMessage("\2473" + "Use command: " + "\2479" + CommandManager.cmdPrefix + "prefix <target prefix>" + "\2473" + " to set command prefix");
        ChatUtil.printChatMessage("\2473" + "List all available commands: " + "\2479" + CommandManager.cmdPrefix + "commands");
    }

    @Override
    public String getSyntax() {
        return "help";
    }

}