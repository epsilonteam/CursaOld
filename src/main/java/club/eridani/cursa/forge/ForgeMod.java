package club.eridani.cursa.forge;

import club.eridani.cursa.Cursa;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;

@Mod(name = Cursa.MOD_NAME,modid = Cursa.MOD_ID,version = Cursa.MOD_VERSION)
public class ForgeMod {

    @Mod.EventHandler
    public void registerForgeEventProcessor(FMLPostInitializationEvent event){
        MinecraftForge.EVENT_BUS.register(ForgeEventProcessor.getInstance());
    }

}
