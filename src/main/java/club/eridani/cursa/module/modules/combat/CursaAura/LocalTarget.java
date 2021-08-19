package club.eridani.cursa.module.modules.combat.CursaAura;

import club.eridani.cursa.utils.Timer;
import club.eridani.cursa.utils.math.Pair;
import net.minecraft.entity.item.EntityEnderCrystal;

import java.util.concurrent.LinkedBlockingDeque;

public class LocalTarget {

    final LinkedBlockingDeque<Pair<EntityEnderCrystal, Timer>> attackTargets = new LinkedBlockingDeque<>();
    final LinkedBlockingDeque<Pair<CrystalTarget, Timer>> placeTargets = new LinkedBlockingDeque<>();

    public synchronized void checkAll(int placeDelay, int attackDelay) {
        synchronized (attackTargets) {
            synchronized (placeTargets) {
                attackTargets.removeIf(it -> it.b.passed(attackDelay));
                placeTargets.removeIf(it -> it.b.passed(placeDelay));
            }
        }
    }

    public synchronized void putAttackTarget(EntityEnderCrystal attackTarget) {
        synchronized (attackTargets) {
            attackTargets.add(new Pair<>(attackTarget, new Timer().reset()));
        }
    }

    public synchronized EntityEnderCrystal getAttackTarget() {
        synchronized (attackTargets) {
            Pair<EntityEnderCrystal, Timer> pair = attackTargets.pollLast();
            return pair == null ? null : pair.a;
        }
    }

    public synchronized void putPlaceTarget(CrystalTarget placeTarget) {
        synchronized (placeTargets) {
            placeTargets.add(new Pair<>(placeTarget, new Timer().reset()));
        }
    }

    public synchronized CrystalTarget getPlaceTarget(boolean shouldIgnoreEntity) {
        synchronized (placeTargets) {
            while (true) {
                Pair<CrystalTarget, Timer> pair = placeTargets.pollLast();
                if (pair == null) break;
                if (pair.a.ignoredEntity == shouldIgnoreEntity) return pair.a;
            }
            return CursaAura.INSTANCE.calculator(CursaAura.INSTANCE.shouldIgnoreEntity.get());
        }
    }

}