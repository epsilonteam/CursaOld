package club.eridani.cursa.asm;

import net.minecraftforge.fml.common.asm.transformers.AccessTransformer;

import java.io.IOException;

public class CursaAccessTransformer extends AccessTransformer {
    public CursaAccessTransformer() throws IOException {
        super("cursa_asm.cfg");
    }
}