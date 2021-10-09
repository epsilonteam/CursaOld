package net.spartanb312.cursa.module.modules.misc;

import net.spartanb312.cursa.common.annotations.ModuleInfo;
import net.spartanb312.cursa.common.annotations.Parallel;
import net.spartanb312.cursa.module.Category;
import net.spartanb312.cursa.module.Module;
import net.spartanb312.cursa.setting.Setting;
import net.minecraft.entity.player.EnumPlayerModelParts;

import java.util.Random;

@Parallel(runnable = true)
@ModuleInfo(name = "SkinFlicker", category = Category.MISC, description = "Dynamic skin")
public class SkinFlicker extends Module {

    Setting<String> mode = setting("Mode", "HORIZONTAL", listOf("HORIZONTAL", "VERTICAL", "RANDOM"));
    Setting<Integer> slowness = setting("Slowness", 2, 1, 10);

    private final static EnumPlayerModelParts[] PARTS_HORIZONTAL = new EnumPlayerModelParts[]{
            EnumPlayerModelParts.LEFT_SLEEVE,
            EnumPlayerModelParts.JACKET,
            EnumPlayerModelParts.HAT,
            EnumPlayerModelParts.LEFT_PANTS_LEG,
            EnumPlayerModelParts.RIGHT_PANTS_LEG,
            EnumPlayerModelParts.RIGHT_SLEEVE
    };

    private final static EnumPlayerModelParts[] PARTS_VERTICAL = new EnumPlayerModelParts[]{
            EnumPlayerModelParts.HAT,
            EnumPlayerModelParts.JACKET,
            EnumPlayerModelParts.LEFT_SLEEVE,
            EnumPlayerModelParts.RIGHT_SLEEVE,
            EnumPlayerModelParts.LEFT_PANTS_LEG,
            EnumPlayerModelParts.RIGHT_PANTS_LEG,
    };

    private final Random r = new Random();
    private final int len = EnumPlayerModelParts.values().length;

    @Override
    public void onTick() {
        if (mc.player == null) return;
        switch (mode.getValue()) {
            case "RANDOM":
                if (mc.player.ticksExisted % slowness.getValue() != 0) return;
                mc.gameSettings.switchModelPartEnabled(EnumPlayerModelParts.values()[r.nextInt(len)]);
                break;
            case "VERTICAL":
            case "HORIZONTAL":
                int i = (mc.player.ticksExisted / slowness.getValue()) % (PARTS_HORIZONTAL.length * 2); // *2 for on/off
                boolean on = false;
                if (i >= PARTS_HORIZONTAL.length) {
                    on = true;
                    i -= PARTS_HORIZONTAL.length;
                }
                mc.gameSettings.setModelPartEnabled(mode.getValue().equals("VERTICAL") ? PARTS_VERTICAL[i] : PARTS_HORIZONTAL[i], on);
        }
    }

}
