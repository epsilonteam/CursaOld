package club.eridani.cursa.utils;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.tree.*;

import static org.objectweb.asm.ClassWriter.COMPUTE_FRAMES;
import static org.objectweb.asm.ClassWriter.COMPUTE_MAXS;

public class ASMUtil {
    public static AbstractInsnNode findMethodInsn(MethodNode mn, int opcode, String owner, String name, String desc) {
        for (AbstractInsnNode insn : mn.instructions.toArray()) {
            if (insn instanceof MethodInsnNode) {
                final MethodInsnNode method = (MethodInsnNode) insn;
                if (method.getOpcode() == opcode && method.owner.equals(owner) && method.name.equals(name) && method.desc.equals(desc)) {
                    return insn;
                }
            }
        }
        return null;
    }

    public static byte[] toBytes(ClassNode classNode) {
        ClassWriter writer = new ClassWriter(COMPUTE_MAXS | COMPUTE_FRAMES);
        classNode.accept(writer);
        return writer.toByteArray();
    }

    public static MethodNode findMethod(ClassNode classNode, String name, String desc) {
        for (MethodNode methodNode : classNode.methods) {
            if (methodNode.name.equals(name) && methodNode.desc.equals(desc)) {
                return methodNode;
            }
        }
        return null;
    }

    public static ClassNode getNode(byte[] classBuffer) {
        ClassNode classNode = new ClassNode();
        ClassReader reader = new ClassReader(classBuffer);
        reader.accept(classNode, 0);
        return classNode;
    }
}
