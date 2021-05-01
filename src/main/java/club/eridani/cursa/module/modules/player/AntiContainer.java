package club.eridani.cursa.module.modules.player;

import club.eridani.cursa.event.events.network.PacketEvent;
import club.eridani.cursa.event.system.Listener;
import club.eridani.cursa.module.Category;
import club.eridani.cursa.module.ModuleBase;
import club.eridani.cursa.module.Module;
import club.eridani.cursa.setting.Setting;
import net.minecraft.block.BlockShulkerBox;
import net.minecraft.client.Minecraft;
import net.minecraft.init.Blocks;
import net.minecraft.network.play.client.CPacketPlayerTryUseItemOnBlock;
import net.minecraft.util.math.BlockPos;

@Module(name = "AntiContainer", category = Category.PLAYER)
public class AntiContainer extends ModuleBase {

    Setting<Boolean> Chest = setting("Chest", true);
    Setting<Boolean> EnderChest = setting("EnderChest", true);
    Setting<Boolean> Trapped_Chest = setting("Trapped_Chest", true);
    Setting<Boolean> Hopper = setting("Hopper", true);
    Setting<Boolean> Dispenser = setting("Dispenser", true);
    Setting<Boolean> Furnace = setting("Furnace", true);
    Setting<Boolean> Beacon = setting("Beacon", true);
    Setting<Boolean> Crafting_Table = setting("Crafting_Table", true);
    Setting<Boolean> Anvil = setting("Anvil", true);
    Setting<Boolean> Enchanting_table = setting("Enchanting_table", true);
    Setting<Boolean> Brewing_Stand = setting("Brewing_Stand", true);
    Setting<Boolean> ShulkerBox = setting("ShulkerBox", true);

    @Listener
    public void onCheck(PacketEvent.Send packet) {
        if (packet.packet instanceof CPacketPlayerTryUseItemOnBlock) {
            BlockPos pos = ((CPacketPlayerTryUseItemOnBlock) packet.packet).getPos();
            if (check(pos)) packet.cancel();
        }
    }

    public boolean check(BlockPos pos) {
        return ((Minecraft.getMinecraft().world.getBlockState(pos).getBlock() == Blocks.CHEST && Chest.getValue())
                || (Minecraft.getMinecraft().world.getBlockState(pos).getBlock() == Blocks.ENDER_CHEST && EnderChest.getValue())
                || (Minecraft.getMinecraft().world.getBlockState(pos).getBlock() == Blocks.TRAPPED_CHEST && Trapped_Chest.getValue())
                || (Minecraft.getMinecraft().world.getBlockState(pos).getBlock() == Blocks.HOPPER && Hopper.getValue())
                || (Minecraft.getMinecraft().world.getBlockState(pos).getBlock() == Blocks.DISPENSER && Dispenser.getValue())
                || (Minecraft.getMinecraft().world.getBlockState(pos).getBlock() == Blocks.FURNACE && Furnace.getValue())
                || (Minecraft.getMinecraft().world.getBlockState(pos).getBlock() == Blocks.BEACON && Beacon.getValue())
                || (Minecraft.getMinecraft().world.getBlockState(pos).getBlock() == Blocks.CRAFTING_TABLE && Crafting_Table.getValue())
                || (Minecraft.getMinecraft().world.getBlockState(pos).getBlock() == Blocks.ANVIL && Anvil.getValue())
                || (Minecraft.getMinecraft().world.getBlockState(pos).getBlock() == Blocks.ENCHANTING_TABLE && Enchanting_table.getValue())
                || (Minecraft.getMinecraft().world.getBlockState(pos).getBlock() == Blocks.BREWING_STAND && Brewing_Stand.getValue())
                || (Minecraft.getMinecraft().world.getBlockState(pos).getBlock() instanceof BlockShulkerBox) && ShulkerBox.getValue());
    }
}
