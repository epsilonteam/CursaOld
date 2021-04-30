package club.eridani.cursa.utils;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;

public class EntityUtil {

    public static Minecraft mc = Minecraft.getMinecraft();

    public static boolean isFakeLocalPlayer(Entity entity) {
        return entity != null && entity.getEntityId() == -100 && mc.player != entity;
    }

}
