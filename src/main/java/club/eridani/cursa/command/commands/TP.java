package club.eridani.cursa.command.commands;

import club.eridani.cursa.command.CommandBase;
import club.eridani.cursa.common.annotations.Command;
import club.eridani.cursa.utils.ChatUtil;

/**
 * Created by B_312 on 01/03/21
 * Updated by B_312 on 01/15/21
 */
@Command(command = "tp", description = "Teleport you to the place you want.")
public class TP extends CommandBase {

    @Override
    public void onCall(String s, String[] args) {
        if (args.length < 3) {
            ChatUtil.printErrorChatMessage(getSyntax());
            return;
        }
        try {
            int x = Integer.parseInt(args[0]);
            int y = Integer.parseInt(args[1]);
            int z = Integer.parseInt(args[2]);
            mc.player.setPosition(x, y, z);
        } catch (Exception e) {
            ChatUtil.printErrorChatMessage(getSyntax());
        }
    }

    @Override
    public String getSyntax() {
        return "tp <x> <y> <z>";
    }

}
