package club.eridani.cursa.module.modules.combat;

import club.eridani.cursa.client.FriendManager;
import club.eridani.cursa.client.GUIManager;
import club.eridani.cursa.common.annotations.Module;
import club.eridani.cursa.event.events.network.PacketEvent;
import club.eridani.cursa.event.events.render.RenderEvent;
import club.eridani.cursa.module.Category;
import club.eridani.cursa.module.ModuleBase;
import club.eridani.cursa.setting.Setting;
import club.eridani.cursa.utils.*;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.play.client.CPacketPlayerTryUseItemOnBlock;
import net.minecraft.network.play.server.SPacketSoundEffect;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

import java.awt.*;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static club.eridani.cursa.utils.CrystalUtil.calculateDamage;
import static org.lwjgl.opengl.GL11.GL_QUADS;

@Module(name = "AutoCrystal", category = Category.COMBAT)
public class AutoCrystal extends ModuleBase {

    Setting<Boolean> autoSwitch = setting("AutoSwitch", false);
    Setting<Boolean> multiPlace = setting("MultiPlace", false);
    Setting<Boolean> autoPlace = setting("Place", true);
    Setting<Boolean> autoExplode = setting("Explode", true);
    Setting<Integer> placeDelay = setting("PlaceDelay", 50, 0, 1000);
    Setting<Integer> explodeDelay = setting("ExplodeDelay", 35, 0, 1000);
    Setting<Double> placeRange = setting("PlaceRange", 4.5, 0, 8);
    Setting<Double> breakRange = setting("PlaceRange", 5.5, 0, 8);
    Setting<Double> distance = setting("Distance", 7.0, 0, 16);
    Setting<Double> placeMinDmg = setting("PlaceMinDmg", 6.0, 0, 36);
    Setting<Double> placeMaxSelf = setting("PlaceMaxSelf", 8.0, 0, 36);
    Setting<Double> breakMinDmg = setting("BreakMinDmg", 4.0, 0, 36);
    Setting<Double> breakMaxSelf = setting("BreakMaxSelf", 8.0, 0, 36);
    Setting<Boolean> facePlace = setting("FacePlace", false);
    Setting<Double> blastHealth = setting("BlastHealth", 2.0, 0.0, 8).whenTrue(facePlace);
    Setting<Boolean> spoofRotations = setting("SpoofRotation", true);
    Setting<Boolean> renderPlace = setting("RenderBlock", true);

    Timer placeTimer = new Timer();
    Timer breakTimer = new Timer();

    int placements = 0;
    boolean isSpoofingAngles = false;
    boolean togglePitch = false;
    BlockPos renderBlock = null;
    float yaw, pitch;

    @Override
    public void onTick() {
        if (mc.player == null || mc.world == null) return;
        renderBlock = null;
        if (autoExplode.getValue() && breakTimer.passed(explodeDelay.getValue())) {
            breakTimer.reset();
            EntityEnderCrystal explodeTarget = getHittableCrystal();
            if (explodeTarget != null) {
                explodeCrystal(explodeTarget);
                if (!multiPlace.getValue()) return;
                if (placements >= 3) {
                    placements = 0;
                    return;
                }
            } else resetRotation();
        }
        if (autoPlace.getValue()) {
            BlockPos placeTarget = getPlaceTargetPos();
            renderBlock = placeTarget;
            if (placeTimer.passed(placeDelay.getValue())) {
                placeTimer.reset();
                if (placeTarget != null) {
                    placeCrystal(placeTarget);
                    if (isSpoofingAngles && spoofRotations.getValue()) {
                        if (togglePitch) {
                            mc.player.rotationPitch += (float) 4.0E-4;
                            togglePitch = false;
                        } else {
                            mc.player.rotationPitch -= (float) 4.0E-4;
                            togglePitch = true;
                        }
                    }
                } else resetRotation();
            }
        }
    }

    @Override
    public void onRenderWorld(RenderEvent event) {
        int color = new Color(GUIManager.getRed(), GUIManager.getGreen(), GUIManager.getBlue(), 60).getRGB();
        if (renderPlace.getValue() && renderBlock != null) {
            CursaTessellator.prepare(GL_QUADS);
            CursaTessellator.drawFullBox(renderBlock, 1f, color);
            CursaTessellator.release();
        }
    }

    private void setYawAndPitch(float yaw1, float pitch1) {
        yaw = yaw1;
        pitch = pitch1;
        isSpoofingAngles = true;
    }

    private void resetRotation() {
        if (isSpoofingAngles) {
            yaw = mc.player.rotationYaw;
            pitch = mc.player.rotationPitch;
            isSpoofingAngles = false;
        }
    }

    private void lookAtPacket(double px, double py, double pz, EntityPlayer me) {
        float[] v = EntityUtil.calculateLookAt(px, py, pz, me);
        setYawAndPitch(v[0], v[1]);
    }

    @Override
    public void onEnable() {
        placements = 0;
    }

    @Override
    public void onDisable() {
        placeTimer.restart();
        breakTimer.restart();
        resetRotation();
        renderBlock = null;
    }

    @Override
    public void onPacketSend(PacketEvent.Send event) {
        if (mc.player == null || mc.world == null) return;
        if (spoofRotations.getValue()) {
            if (event.packet instanceof CPacketPlayer) {
                CPacketPlayer packet = (CPacketPlayer) event.getPacket();
                if (isSpoofingAngles) {
                    (packet).yaw = yaw;
                    (packet).pitch = pitch;
                }
            }
        }
    }

    @Override
    public void onPacketReceive(PacketEvent.Receive event) {
        if (mc.player == null || mc.world == null) return;
        if (event.packet instanceof SPacketSoundEffect) {
            SPacketSoundEffect packet = (SPacketSoundEffect) event.packet;
            if (packet.getCategory() == SoundCategory.BLOCKS && packet.getSound() == SoundEvents.ENTITY_GENERIC_EXPLODE) {
                for (Entity e : Minecraft.getMinecraft().world.loadedEntityList) {
                    if (e instanceof EntityEnderCrystal) {
                        if (e.getDistance(packet.getX(), packet.getY(), packet.getZ()) <= 6.0f) {
                            e.setDead();
                        }
                    }
                }
            }
        }
    }

    public EntityEnderCrystal getHittableCrystal() {
        return mc.world.loadedEntityList.stream()
                .filter(it -> it instanceof EntityEnderCrystal)
                .filter(it -> mc.player.getDistance(it) <= breakRange.getValue())
                .filter(it -> canHitCrystal(it.getPositionVector()))
                .map(e -> (EntityEnderCrystal) e)
                .min(Comparator.comparing(e -> mc.player.getDistance(e))).orElse(null);
    }

    public boolean canHitCrystal(Vec3d crystal) {
        float selfDamage = CrystalUtil.calculateDamage(crystal.x, crystal.y, crystal.z, mc.player, mc.player.getPositionVector());
        if (selfDamage >= mc.player.getHealth() + mc.player.getAbsorptionAmount()) return false;
        List<EntityPlayer> entities = mc.world.playerEntities.stream()
                .filter(e -> mc.player.getDistance(e) <= distance.getValue())
                .filter(e -> mc.player != e)
                .filter(e -> !FriendManager.isFriend(e))
                .sorted(Comparator.comparing(e -> mc.player.getDistance(e)))
                .collect(Collectors.toList());
        for (EntityPlayer player : entities) {
            if (player.isDead || (player.getHealth() + player.getAbsorptionAmount()) <= 0.0f) continue;
            double minDamage = breakMinDmg.getValue();
            if (canFacePlace(player, blastHealth.getValue())) minDamage = 1;
            double targetDamage = CrystalUtil.calculateDamage(crystal.x, crystal.y, crystal.z, player, player.getPositionVector());
            if (targetDamage > player.getHealth() + player.getAbsorptionAmount()
                    && selfDamage < mc.player.getHealth() + mc.player.getAbsorptionAmount()
            ) return true;
            if (selfDamage > breakMaxSelf.getValue()) continue;
            if (targetDamage < minDamage) continue;
            if (selfDamage > targetDamage) continue;
            return true;
        }
        return false;
    }

    public boolean canFacePlace(EntityLivingBase target, double blast) {
        float healthTarget = target.getHealth() + target.getAbsorptionAmount();
        return healthTarget <= blast;
    }

    public void explodeCrystal(EntityEnderCrystal crystal) {
        lookAtPacket(crystal.posX, crystal.posY, crystal.posZ, mc.player);
        mc.playerController.attackEntity(mc.player, crystal);
        mc.player.swingArm(mc.player.getHeldItemOffhand().getItem() == Items.END_CRYSTAL ? EnumHand.OFF_HAND : EnumHand.MAIN_HAND);
        mc.player.resetCooldown();
    }

    public BlockPos getPlaceTargetPos() {
        BlockPos targetPos;
        List<BlockPos> blockPoses = findCrystalBlocks();
        List<EntityPlayer> players = mc.world.playerEntities.stream()
                .filter(e -> mc.player.getDistance(e) <= distance.getValue())
                .filter(e -> mc.player != e)
                .filter(e -> !FriendManager.isFriend(e))
                .sorted(Comparator.comparing(e -> mc.player.getDistance(e)))
                .collect(Collectors.toList());
        targetPos = calculateTargetPlace(blockPoses, players);
        return targetPos;
    }

    public BlockPos calculateTargetPlace(List<BlockPos> crystalBlocks, List<EntityPlayer> players) {
        double maxDamage = 0.5;
        BlockPos targetBlock = null;
        for (Entity it : players) {
            if (it != mc.player) {
                if (((EntityLivingBase) it).getHealth() <= 0.0f) continue;
                for (BlockPos blockPos : crystalBlocks) {
                    if (it.getDistanceSq(blockPos) >= distance.getValue() * distance.getValue()) continue;
                    if (mc.player.getDistance(blockPos.getX(), blockPos.getY(), blockPos.getZ()) > placeRange.getValue())
                        continue;
                    double targetDamage = calculateDamage(blockPos.getX() + 0.5, blockPos.getY() + 1, blockPos.getZ() + 0.5, it);
                    if (targetDamage < maxDamage) continue;
                    if (targetDamage < (facePlace.getValue() ? (canFacePlace((EntityLivingBase) it, blastHealth.getValue()) ? 1 : placeMinDmg.getValue()) : placeMinDmg.getValue()))
                        continue;
                    float healthTarget = ((EntityLivingBase) it).getHealth() + ((EntityLivingBase) it).getAbsorptionAmount();
                    float healthSelf = mc.player.getHealth() + mc.player.getAbsorptionAmount();
                    double selfDamage = calculateDamage(blockPos.getX() + 0.5, blockPos.getY() + 1, blockPos.getZ() + 0.5, mc.player);
                    if (selfDamage > targetDamage && targetDamage < healthTarget) continue;
                    if (selfDamage - 0.5 > healthSelf) continue;
                    if (selfDamage > placeMaxSelf.getValue()) continue;
                    maxDamage = targetDamage;
                    targetBlock = blockPos;
                }
                if (targetBlock != null) break;
            }
        }
        return targetBlock;
    }

    public boolean canPlaceCrystal(BlockPos blockPos) {
        BlockPos boost = blockPos.add(0, 1, 0);
        BlockPos boost2 = blockPos.add(0, 2, 0);
        if (mc.world.getBlockState(blockPos).getBlock() != Blocks.BEDROCK && mc.world.getBlockState(blockPos).getBlock() != Blocks.OBSIDIAN)
            return false;
        if (mc.world.getBlockState(boost).getBlock() != Blocks.AIR || mc.world.getBlockState(boost2).getBlock() != Blocks.AIR)
            return false;
        return mc.world.getEntitiesWithinAABB(Entity.class, new AxisAlignedBB(boost)).isEmpty()
                && mc.world.getEntitiesWithinAABB(Entity.class, new AxisAlignedBB(boost2)).isEmpty();
    }

    private List<BlockPos> findCrystalBlocks() {
        double range = distance.getValue();
        NonNullList<BlockPos> positions = NonNullList.create();
        positions.addAll(BlockInteractionHelper.getSphere(CrystalUtil.getPlayerPos(), (float) range, (int) range, false, true, 0)
                .stream()
                .filter(this::canPlaceCrystal)
                .sorted(Comparator.comparing(it -> mc.player.getDistance(it.getX(), it.getY(), it.getZ())))
                .collect(Collectors.toList()));
        return positions;
    }

    public void placeCrystal(BlockPos blockPos) {
        boolean isOffhand = mc.player.getHeldItemOffhand().getItem() == Items.END_CRYSTAL;
        EnumHand enumHand = isOffhand ? EnumHand.OFF_HAND : EnumHand.MAIN_HAND;
        if (autoSwitch.getValue() && !isOffhand && mc.player.getHeldItemMainhand().getItem() != Items.END_CRYSTAL) {
            int crystalSlot = -1;
            for (int l = 0; l < 9; ++l) {
                if (mc.player.inventory.getStackInSlot(l).getItem() == Items.END_CRYSTAL) {
                    crystalSlot = l;
                    break;
                }
            }
            if (crystalSlot != -1) mc.player.inventory.currentItem = crystalSlot;
            resetRotation();
            return;
        }
        if (isOffhand || mc.player.getHeldItemMainhand().getItem() == Items.END_CRYSTAL) {
            lookAtPacket(blockPos.x + 0.5, blockPos.y - 0.5, blockPos.z + 0.5, mc.player);
            mc.player.connection.sendPacket(new CPacketPlayerTryUseItemOnBlock(blockPos, EnumFacing.UP, enumHand, 0, 0, 0));
        }
        placements++;
    }

}
