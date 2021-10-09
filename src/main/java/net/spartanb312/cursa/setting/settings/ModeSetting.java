package net.spartanb312.cursa.setting.settings;

import net.spartanb312.cursa.setting.Setting;

import java.util.ArrayList;
import java.util.List;

public class ModeSetting extends Setting<String> {

    private final List<String> modes = new ArrayList<>();

    public ModeSetting(String name, String defaultValue, List<String> modes) {
        super(name, defaultValue);
        if (!modes.contains(defaultValue)) modes.add(defaultValue);
        modes.forEach(it -> {
            if (!this.modes.contains(it)) this.modes.add(it);
        });
    }

    public boolean toggled(String modeName) {
        return this.getValue().equals(modeName);
    }

    @Override
    public void setValue(String modeName) {
        if (modes.contains(modeName)) {
            this.value = modeName;
        }
    }

    public void forwardLoop() {
        int index = modes.indexOf(value);
        if (index == modes.size() - 1) {
            value = modes.get(0);
        } else {
            value = modes.get(index + 1);
        }
    }

}
