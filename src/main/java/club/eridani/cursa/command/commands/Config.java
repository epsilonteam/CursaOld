package club.eridani.cursa.command.commands;

import club.eridani.cursa.command.Command;
import club.eridani.cursa.command.CommandBase;
import club.eridani.cursa.tasks.ConfigOperateTask;
import club.eridani.cursa.utils.ChatUtil;

import static club.eridani.cursa.concurrent.TaskManager.launch;

/**
 * Created by killRED on 2020
 * Updated by B_312 on 01/15/21
 */
@Command(command = "config", description = "Save or load config.")
public class Config extends CommandBase {

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
        launch(new ConfigOperateTask(ConfigOperateTask.Operation.Load));
        ChatUtil.sendNoSpamMessage("Configuration reloaded!");
    }

    public void save() {
        launch(new ConfigOperateTask(ConfigOperateTask.Operation.Save));
        ChatUtil.sendNoSpamMessage("Configuration saved!");
    }

}
