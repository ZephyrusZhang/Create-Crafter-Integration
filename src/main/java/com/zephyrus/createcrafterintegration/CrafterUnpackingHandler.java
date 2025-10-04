package com.zephyrus.createcrafterintegration;

import com.simibubi.create.api.packager.unpacking.UnpackingHandler;
import com.simibubi.create.content.logistics.BigItemStack;
import com.simibubi.create.content.logistics.stockTicker.PackageOrderWithCrafts;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.items.IItemHandler;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CrafterUnpackingHandler implements UnpackingHandler {

    @Override
    public boolean unpack(Level level, BlockPos pos, BlockState state, Direction side, List<ItemStack> items, @Nullable PackageOrderWithCrafts orderContext, boolean simulate) {
        if (orderContext.orderedCrafts().isEmpty()) return false;
        CreateCrafterIntegration.LOGGER.info("testing unpacking handler");
        IItemHandler handler = level.getCapability(Capabilities.ItemHandler.BLOCK, pos, side);
        PackageOrderWithCrafts.CraftingEntry craftRecipe = orderContext.orderedCrafts().getFirst();
        List<BigItemStack> itemStacks = orderContext.orderedStacks().stacks();
        Map<Item, BigItemStack> mapping = new HashMap<>();
        for (BigItemStack stack : itemStacks)
            mapping.put(stack.stack.getItem(), stack);

        if (craftRecipe.count() > 64) return false;
        for (int i = 0; i < handler.getSlots(); i++) {
            ItemStack craftPatternItem = craftRecipe.pattern().stacks().get(i).stack;
            if (craftPatternItem.isEmpty()) continue;

            BigItemStack item = mapping.get(craftPatternItem.getItem());
            if (item.count == 0) continue;
            ItemStack itemToInsert = item.stack.copy();
            itemToInsert.setCount(craftRecipe.count());
            if (simulate) {
                if (!handler.getStackInSlot(i).isEmpty() || !handler.insertItem(i, itemToInsert, true).isEmpty()) {
                    return false;
                }
            } else {
                if (handler.getStackInSlot(i).isEmpty() && handler.insertItem(i, itemToInsert, false).isEmpty()) {
                    item.count -= craftRecipe.count();
                } else return false;
            }
        }
        return true;
    }

}
