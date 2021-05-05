package club.eridani.cursa.asm;

import club.eridani.cursa.asm.api.ClassPatch;
import club.eridani.cursa.asm.impl.PatchEntityRenderer;
import net.minecraft.launchwrapper.IClassTransformer;

import java.util.ArrayList;
import java.util.List;

public class CursaTransformer implements IClassTransformer {

    public static final List<ClassPatch> patches = new ArrayList<>();

    static {
        patches.add(new PatchEntityRenderer());
    }

    @Override
    public byte[] transform(String name, String transformedName, byte[] bytes) {
        if (bytes == null)
            return null;

        for (ClassPatch it : patches) {
            if (it.className.equals(transformedName)) {
                return it.transform(bytes);
            }
        }

        return bytes;
    }

}