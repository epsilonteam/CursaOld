package club.eridani.cursa.module.modules.player;

import club.eridani.cursa.common.annotations.Module;
import club.eridani.cursa.common.annotations.Parallel;
import club.eridani.cursa.module.Category;
import club.eridani.cursa.module.ModuleBase;
import club.eridani.cursa.setting.Setting;
import com.mojang.authlib.GameProfile;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.potion.PotionEffect;

import java.util.UUID;

@Parallel
@Module(name = "FakePlayer", category = Category.PLAYER)
public class FakePlayer extends ModuleBase {

    public static String customName = "None";
    Setting<Integer> health = setting("Health", 10, 0, 36);
    Setting<Boolean> customMode = setting("CustomName", false);
    Setting<String> mode = setting("Name", "B_312",
            "B_312",
            "popbob",
            "jared2013",
            "bachi",
            "dot5",
            "FitMC",
            "Cyri").whenFalse(customMode);

    @Override
    public void onEnable() {
        if (mc.player == null || mc.world == null) return;
        EntityOtherPlayerMP fakePlayer = new EntityOtherPlayerMP(mc.world, new GameProfile(UUID.fromString("60569353-f22b-42da-b84b-d706a65c5ddf"), customMode.getValue() ? customName : mode.getValue()));
        fakePlayer.copyLocationAndAnglesFrom(mc.player);
        for (PotionEffect potionEffect : mc.player.getActivePotionEffects()) {
            fakePlayer.addPotionEffect(potionEffect);
        }
        fakePlayer.setHealth(health.getValue());
        fakePlayer.inventory.copyInventory(mc.player.inventory);
        fakePlayer.rotationYawHead = mc.player.rotationYawHead;
        mc.world.addEntityToWorld(-100, fakePlayer);
    }

    @Override
    public void onDisable() {
        mc.world.removeEntityFromWorld(-100);
    }

}
