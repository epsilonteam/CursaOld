package club.eridani.cursa.utils;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityIronGolem;
import net.minecraft.entity.passive.EntityAmbientCreature;
import net.minecraft.entity.passive.EntitySquid;
import net.minecraft.entity.passive.EntityWolf;

public class EntityUtil {

    public static Minecraft mc = Minecraft.getMinecraft();

    public static boolean isFakeLocalPlayer(Entity entity) {
        return entity != null && entity.getEntityId() == -100 && mc.player != entity;
    }

    public static boolean isPassive(Entity e) {
        if (e instanceof EntityWolf && ((EntityWolf) e).isAngry()) {
            return false;
        }
        if (e instanceof EntityAgeable || e instanceof EntityAmbientCreature || e instanceof EntitySquid) {
            return true;
        }
        return e instanceof EntityIronGolem && ((EntityIronGolem) e).getRevengeTarget() == null;
    }

    public static boolean isLiving(Entity e) {
        return e instanceof EntityLivingBase;
    }

}
