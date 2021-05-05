package club.eridani.cursa.mixin;

import club.eridani.cursa.Cursa;
import club.eridani.cursa.asm.CursaAccessTransformer;
import club.eridani.cursa.asm.CursaTransformer;
import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.spongepowered.asm.launch.MixinBootstrap;
import org.spongepowered.asm.mixin.MixinEnvironment;
import org.spongepowered.asm.mixin.Mixins;

import java.util.Map;

@IFMLLoadingPlugin.TransformerExclusions({"club.eridani.cursa.asm"})
@IFMLLoadingPlugin.MCVersion("1.12.2")
@IFMLLoadingPlugin.Name("Cursa")
@IFMLLoadingPlugin.SortingIndex(value = 1001)
public class MixinLoader implements IFMLLoadingPlugin {

    public static final Logger log = LogManager.getLogger("MIXIN");

    public MixinLoader() {
        Cursa.getInstance();
        log.info("Cursa mixins initialized");
        MixinBootstrap.init();
        Mixins.addConfigurations("mixins.cursa.json");
        MixinEnvironment.getDefaultEnvironment().setObfuscationContext("searge");
        log.info(MixinEnvironment.getDefaultEnvironment().getObfuscationContext());
    }

    @Override
    public String[] getASMTransformerClass() {
        return new String[]{
                CursaTransformer.class.getName()
        };
    }

    @Override
    public String getModContainerClass() {
        return null;
    }

    @Override
    public String getSetupClass() {
        return null;
    }

    @Override
    public void injectData(Map<String, Object> data) { }

    @Override
    public String getAccessTransformerClass() {
        return CursaAccessTransformer.class.getName();
    }

}
