package net.spartanb312.cursa.setting.settings;

import net.spartanb312.cursa.concurrent.task.VoidTask;
import net.spartanb312.cursa.setting.Setting;

public class ActionSetting extends Setting<VoidTask> {
    public ActionSetting(String name, VoidTask defaultValue) {
        super(name, defaultValue);
    }
}
