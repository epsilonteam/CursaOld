package net.spartanb312.cursa.setting.settings;

import net.spartanb312.cursa.setting.Setting;
import net.spartanb312.cursa.utils.KeyBind;

public class BindSetting extends Setting<KeyBind> {
    public BindSetting(String name, KeyBind defaultValue) {
        super(name, defaultValue);
    }
}
