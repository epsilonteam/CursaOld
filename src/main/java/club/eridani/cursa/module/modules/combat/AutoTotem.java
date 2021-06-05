package club.eridani.cursa.module.modules.combat;

import club.eridani.cursa.common.annotations.Module;
import club.eridani.cursa.common.annotations.ParallelLoadable;
import club.eridani.cursa.common.annotations.ParallelRunnable;
import club.eridani.cursa.common.types.Tick;
import club.eridani.cursa.module.Category;
import club.eridani.cursa.module.ModuleBase;
import club.eridani.cursa.setting.Setting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.init.Items;
import net.minecraft.inventory.ClickType;
import net.minecraft.item.ItemStack;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

@ParallelLoadable
@Module(name = "AutoTotem",category = Category.COMBAT)
public class AutoTotem extends ModuleBase {

    Setting<Boolean> soft = setting("Soft",true);
    Setting<Boolean> pauseInContainers = setting("PauseInInventory",false);
    Setting<Boolean> pauseInInventory = setting("PauseInContainer",false);
	
	private int numOfTotems;
    private int preferredTotemSlot;
	public static Minecraft mc = Minecraft.getMinecraft();

    @ParallelRunnable(type = Tick.Loop)
    public void onParallelTick() {

        if (mc.player == null) {
            return;
        }
        if (!findTotems()) {
            return;
        }
        if (pauseInContainers.getValue() && (mc.currentScreen instanceof GuiContainer) && !(mc.currentScreen instanceof GuiInventory)) {
            return;
        }
        if (pauseInInventory.getValue() && (mc.currentScreen instanceof GuiInventory)) {
            return;
        }

        if (soft.getValue()) {
            if (mc.player.getHeldItemOffhand().getItem().equals(Items.AIR)) {
                mc.playerController.windowClick(0, preferredTotemSlot, 0, ClickType.PICKUP, mc.player);
                mc.playerController.windowClick(0, 45, 0, ClickType.PICKUP, mc.player);
                mc.playerController.updateController();
            }

        } else {

            if (!mc.player.getHeldItemOffhand().getItem().equals(Items.TOTEM_OF_UNDYING)) {

                boolean offhandEmptyPreSwitch = false;

                if (mc.player.getHeldItemOffhand().getItem().equals(Items.AIR)) {
                    offhandEmptyPreSwitch = true;
                }
                mc.playerController.windowClick(0, preferredTotemSlot, 0, ClickType.PICKUP, mc.player);
                mc.playerController.windowClick(0, 45, 0, ClickType.PICKUP, mc.player);

                if (!offhandEmptyPreSwitch) {
                    mc.playerController.windowClick(0, preferredTotemSlot, 0, ClickType.PICKUP, mc.player);
                }
                mc.playerController.updateController();

            }

        }

    }

    private boolean findTotems() {
        this.numOfTotems = 0;
        AtomicInteger preferredTotemSlotStackSize = new AtomicInteger();
        preferredTotemSlotStackSize.set(Integer.MIN_VALUE);
        getInventoryAndHotbarSlots().forEach((slotKey, slotValue) -> {
            int numOfTotemsInStack = 0;
            if (slotValue.getItem().equals(Items.TOTEM_OF_UNDYING)) {
                numOfTotemsInStack = slotValue.getCount();
                if (preferredTotemSlotStackSize.get() < numOfTotemsInStack) {
                    preferredTotemSlotStackSize.set(numOfTotemsInStack);
                    preferredTotemSlot = slotKey;
                }
            }

            this.numOfTotems = this.numOfTotems + numOfTotemsInStack;

        });
        if (mc.player.getHeldItemOffhand().getItem().equals(Items.TOTEM_OF_UNDYING)) {
            this.numOfTotems = this.numOfTotems + mc.player.getHeldItemOffhand().getCount();
        }
        return this.numOfTotems != 0;
    }


    private static Map<Integer, ItemStack> getInventoryAndHotbarSlots() {
        return getInventorySlots(9, 44);
    }

    private static Map<Integer, ItemStack> getInventorySlots(int current, int last) {
        Map<Integer, ItemStack> fullInventorySlots = new HashMap<>();
        while (current <= last) {
            fullInventorySlots.put(current, mc.player.inventoryContainer.getInventory().get(current));
            current++;
        }
        return fullInventorySlots;
    }

}
