package club.eridani.cursa.module.modules.combat.CursaAura;

import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;

public class CrystalTarget {
    public BlockPos blockPos;
    public Entity target;
    public boolean ignoredEntity;

    public CrystalTarget(BlockPos block, Entity target, boolean ignoredEntity) {
        this.blockPos = block;
        this.target = target;
        this.ignoredEntity = ignoredEntity;
    }
}