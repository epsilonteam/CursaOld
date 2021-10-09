package net.spartanb312.cursa.setting.settings;

import net.spartanb312.cursa.setting.NumberSetting;

public class IntSetting extends NumberSetting<Integer> {
    public IntSetting(String name, int defaultValue, int min, int max) {
        super(name, defaultValue, min, max);
    }
}
