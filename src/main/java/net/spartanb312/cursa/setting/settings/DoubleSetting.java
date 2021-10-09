package net.spartanb312.cursa.setting.settings;

import net.spartanb312.cursa.setting.NumberSetting;

public class DoubleSetting extends NumberSetting<Double> {
    public DoubleSetting(String name, double defaultValue, double min, double max) {
        super(name, defaultValue, min, max);
    }
}
