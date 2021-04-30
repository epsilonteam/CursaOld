package club.eridani.cursa.setting.settings;

import club.eridani.cursa.setting.NumberSetting;

public class IntSetting extends NumberSetting<Integer> {
    public IntSetting(String name, int defaultValue, int min, int max) {
        super(name, defaultValue, min, max);
    }
}
