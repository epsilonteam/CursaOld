package club.eridani.cursa.asm.impl;

import club.eridani.cursa.Cursa;
import club.eridani.cursa.asm.api.ClassPatch;
import club.eridani.cursa.asm.api.MappingName;
import club.eridani.cursa.event.events.render.RenderOverlayEvent;
import club.eridani.cursa.utils.ASMUtil;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.*;

import static org.spongepowered.asm.lib.Opcodes.*;

public class PatchEntityRenderer extends ClassPatch {

    public PatchEntityRenderer() {
        super("net.minecraft.client.renderer.EntityRenderer","buq");
    }

    @Override
    public byte[] transform(byte[] bytes) {
        ClassNode classNode = new ClassNode();
        ClassReader classReader = new ClassReader(bytes);
        classReader.accept(classNode, 0);

        MappingName method = new MappingName("func_181560_a", "a", "updateCameraAndRender");
        String desc = "(FJ)V";

        for (MethodNode it : classNode.methods) {
            if (method.equalName(it.name) && it.desc.equals(desc)) {
                patchUpdateCameraAndRender(it);
            }
        }

        ClassWriter writer = new ClassWriter(0);
        classNode.accept(writer);
        return writer.toByteArray();
    }

    public void patchUpdateCameraAndRender(MethodNode methodNode) {
        final AbstractInsnNode target = ASMUtil.findMethodInsn(methodNode, INVOKEVIRTUAL, "biq", "a", "(F)V");

        if (target != null) {
            final InsnList insnList = new InsnList();
            insnList.add(new VarInsnNode(FLOAD, 1));
            insnList.add(new MethodInsnNode(INVOKESTATIC, Type.getInternalName(this.getClass()), "updateCameraAndRenderHook", "(F)V", false));
            methodNode.instructions.insert(target, insnList);
        }
    }

    public static void updateCameraAndRenderHook(float partialTicks) {
        Cursa.EVENT_BUS.post(new RenderOverlayEvent(partialTicks));
    }

}
