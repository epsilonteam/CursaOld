package club.eridani.cursa.command.commands;

import club.eridani.cursa.client.ModuleManager;
import club.eridani.cursa.command.CommandBase;
import club.eridani.cursa.common.annotations.Command;
import club.eridani.cursa.module.ModuleBase;
import club.eridani.cursa.utils.ChatUtil;
import org.lwjgl.input.Keyboard;

@Command(command = "bind", description = "Set module bind key.")
public class Bind extends CommandBase {

    @Override
    public void onCall(String s, String[] args) {

        if (args.length == 1) {
            ChatUtil.sendNoSpamMessage("Please specify a module.");
            return;
        }

        try {
            String module = args[0];
            String rKey = args[1];

            ModuleBase m = ModuleManager.getModuleByName(module);

            if (m == null) {
                ChatUtil.sendNoSpamMessage("Unknown module '" + module + "'!");
                return;
            }

            if (rKey == null) {
                ChatUtil.sendNoSpamMessage(m.name + " is bound to " + ChatUtil.SECTIONSIGN + "b" + m.keyCode);
                return;
            }

            int key = Keyboard.getKeyIndex(rKey);
            boolean isNone = false;

            if (rKey.equalsIgnoreCase("none")) {
                key = 0;
                isNone = true;
            }

            if (key == 0 && !isNone) {
                ChatUtil.sendNoSpamMessage("Unknown key '" + rKey + "'!");
                return;
            }

            m.keyCode = key;
            ChatUtil.sendNoSpamMessage("Bind for " + ChatUtil.SECTIONSIGN + "b" + m.name + ChatUtil.SECTIONSIGN + "r set to " + ChatUtil.SECTIONSIGN + "b" + rKey.toUpperCase());

        } catch (Exception e) {
            ChatUtil.sendNoSpamErrorMessage(getSyntax());
        }
    }

    @Override
    public String getSyntax() {
        return "bind <module> <bind>";
    }

}
