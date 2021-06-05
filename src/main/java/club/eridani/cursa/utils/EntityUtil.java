package club.eridani.cursa.utils;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityIronGolem;
import net.minecraft.entity.passive.EntityAmbientCreature;
import net.minecraft.entity.passive.EntitySquid;
import net.minecraft.entity.passive.EntityWolf;
import net.minecraft.entity.player.EntityPlayer;

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

    public static float[] calculateLookAt(double px, double py, double pz, EntityPlayer me) {
        double dirX = me.posX - px;
        double dirY = me.posY - py;
        double dirZ = me.posZ - pz;
        double len = Math.sqrt(dirX * dirX + dirY * dirY + dirZ * dirZ);
        dirX /= len;
        dirY /= len;
        dirZ /= len;
        double pitch = Math.asin(dirY);
        double yaw = Math.atan2(dirZ, dirX);
        pitch = pitch * 180.0d / Math.PI;
        yaw = yaw * 180.0d / Math.PI;
        yaw += 90f;
        return new float[]{(float) yaw, (float) pitch};
    }

}
