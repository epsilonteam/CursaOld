package net.spartanb312.cursa.event.events.client;

import net.spartanb312.cursa.event.CursaEvent;
import net.spartanb312.cursa.setting.Setting;

public class SettingUpdateEvent extends CursaEvent {

    private final Setting<?> setting;

    public SettingUpdateEvent(Setting<?> setting) {
        this.setting = setting;
    }

    public Setting<?> getSetting() {
        return setting;
    }
}
