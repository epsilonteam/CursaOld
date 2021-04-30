package club.eridani.cursa.setting;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BooleanSupplier;

public class Setting<T> {

    private final String name;
    private final T defaultValue;
    protected T value;
    private final List<BooleanSupplier> visibilities = new ArrayList<>();

    public Setting(String name, T defaultValue) {
        this.name = name;
        this.defaultValue = defaultValue;
        this.value = defaultValue;
    }

    public T getDefaultValue() {
        return defaultValue;
    }

    public T getValue() {
        return value;
    }

    public void setValue(T valueIn) {
        value = valueIn;
    }

    public Setting<T> v(BooleanSupplier booleanSupplier) {
        this.visibilities.add(booleanSupplier);
        return this;
    }

    public Setting<T> r(Setting<Boolean> booleanSetting) {
        return v(() -> !booleanSetting.getValue());
    }

    public Setting<T> b(Setting<Boolean> booleanSetting) {
        return v(booleanSetting::getValue);
    }

    public boolean isVisible() {
        for (BooleanSupplier booleanSupplier : visibilities) {
            if (!booleanSupplier.getAsBoolean()) return false;
        }
        return true;
    }

    public String getName() {
        return name;
    }

}
