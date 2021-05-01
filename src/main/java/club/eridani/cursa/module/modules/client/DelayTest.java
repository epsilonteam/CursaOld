package club.eridani.cursa.module.modules.client;

import club.eridani.cursa.concurrent.repeat.RepeatManager;
import club.eridani.cursa.module.Category;
import club.eridani.cursa.module.Module;
import club.eridani.cursa.module.ModuleBase;
import club.eridani.cursa.utils.ChatUtil;

@Module(name = "DelayTest", category = Category.CLIENT)
public class DelayTest extends ModuleBase {

    @Override
    public void onEnable() {
        RepeatManager.addDelayTask(5000, "Parameter", valueIn -> ChatUtil.printChatMessage("Delay :" + valueIn));
    }
}
