package club.eridani.cursa.module.modules.player;

import club.eridani.cursa.common.annotations.Module;
import club.eridani.cursa.common.annotations.ParallelLoadable;
import club.eridani.cursa.module.Category;
import club.eridani.cursa.module.ModuleBase;

@ParallelLoadable
@Module(name = "AutoJump", category = Category.PLAYER)
public class AutoJump extends ModuleBase {

    @Override
    public void onParallelTick() {
        if(mc.player == null) return;
        if (mc.player.isInWater() || mc.player.isInLava()) mc.player.motionY = 0.1;
        else if (mc.player.onGround) mc.player.jump();
    }

}
