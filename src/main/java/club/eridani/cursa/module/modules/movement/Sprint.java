package club.eridani.cursa.module.modules.movement;

import club.eridani.cursa.module.Category;
import club.eridani.cursa.module.ModuleBase;
import club.eridani.cursa.module.Module;

@Module(name = "Sprint", category = Category.MOVEMENT)
public class Sprint extends ModuleBase {

    @Override
    public void onParallelTick() {
        if (mc.player == null) return;
        try {
            mc.player.setSprinting(!mc.player.collidedHorizontally && mc.player.moveForward > 0);
        } catch (Exception ignored) {
        }
    }

}
