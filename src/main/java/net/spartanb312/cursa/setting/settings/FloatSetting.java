package net.spartanb312.cursa.setting.settings;

import net.spartanb312.cursa.setting.NumberSetting;

public class FloatSetting extends NumberSetting<Float> {
    public FloatSetting(String name, float defaultValue, float min, float max) {
        super(name, defaultValue, min, max);
    }
}
