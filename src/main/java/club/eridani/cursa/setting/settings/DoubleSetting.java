package club.eridani.cursa.setting.settings;

import club.eridani.cursa.setting.NumberSetting;

public class DoubleSetting extends NumberSetting<Double> {
    public DoubleSetting(String name, double defaultValue, double min, double max) {
        super(name, defaultValue, min, max);
    }
}
