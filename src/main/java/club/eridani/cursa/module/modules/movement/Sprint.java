package club.eridani.cursa.module.modules.movement;

import club.eridani.cursa.common.annotations.Module;
import club.eridani.cursa.common.annotations.Parallel;
import club.eridani.cursa.module.Category;
import club.eridani.cursa.module.ModuleBase;

@Parallel(runnable = true)
@Module(name = "Sprint", category = Category.MOVEMENT)
public class Sprint extends ModuleBase {

    @Override
    public void onRenderTick() {
        if (mc.player == null) return;
        mc.player.setSprinting(!mc.player.collidedHorizontally && mc.player.moveForward > 0);
    }

}
