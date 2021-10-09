package net.spartanb312.cursa.utils;

import net.minecraft.client.Minecraft;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class ItemUtils {

    public static int getItemCount(Item item) {
        int count = Minecraft.getMinecraft().player.inventory.mainInventory.stream()
                .filter(itemStack -> itemStack.getItem() == item)
                .mapToInt(ItemStack::getCount).sum();
        if (Minecraft.getMinecraft().player.getHeldItemOffhand().getItem() == item) {
            ++count;
        }
        return count;
    }

}
