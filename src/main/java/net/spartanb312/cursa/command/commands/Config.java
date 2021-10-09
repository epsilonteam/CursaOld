package net.spartanb312.cursa.command.commands;

import net.spartanb312.cursa.command.Command;
import net.spartanb312.cursa.common.annotations.CommandInfo;
import net.spartanb312.cursa.tasks.Tasks;
import net.spartanb312.cursa.utils.ChatUtil;

import static net.spartanb312.cursa.concurrent.TaskManager.launch;

/**
 * Created by killRED on 2020
 * Updated by B_312 on 01/15/21
 */
@CommandInfo(command = "config", description = "Save or load config.")
public class Config extends Command {

    @Override
    public void onCall(String s, String[] args) {
        if (args[0] == null) {
            ChatUtil.sendNoSpamMessage("Missing argument &bmode&r: Choose from reload, save or path");
            return;
        }
        switch (args[0].toLowerCase()) {
            case "save":
                this.save();
                break;
            case "load":
                this.load();
                break;
        }
    }

    @Override
    public String getSyntax() {
        return "config <save/load>";
    }

    public void load() {
        launch(Tasks.LoadConfig);
        ChatUtil.sendNoSpamMessage("Configuration reloaded!");
    }

    public void save() {
        launch(Tasks.SaveConfig);
        ChatUtil.sendNoSpamMessage("Configuration saved!");
    }

}
