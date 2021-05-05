package club.eridani.cursa.asm.api;

public class ClassPatch {
    public final String className;
    public final String notchName;

    public ClassPatch(String nameIn, String notchIn) {
        className = nameIn;
        notchName = notchIn;
    }

    public byte[] transform(byte[] bytes) {
        return new byte[0];
    }

    public boolean equalName(String name) {
        return className.equals(name) || notchName.equals(name);
    }
}
