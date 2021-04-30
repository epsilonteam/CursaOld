package club.eridani.cursa.event.events.client;

import club.eridani.cursa.event.CursaEvent;
import club.eridani.cursa.setting.Setting;

public class SettingUpdateEvent extends CursaEvent {

    private final Setting<?> setting;

    public SettingUpdateEvent(Setting<?> setting) {
        this.setting = setting;
    }

    public Setting<?> getSetting() {
        return setting;
    }
}
