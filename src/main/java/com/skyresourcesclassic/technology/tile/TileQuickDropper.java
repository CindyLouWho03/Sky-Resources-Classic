package com.skyresourcesclassic.technology.tile;

import com.skyresourcesclassic.base.tile.TileItemInventory;
import com.skyresourcesclassic.registry.ModEntities;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ITickable;

public class TileQuickDropper extends TileItemInventory implements ITickable {
    public TileQuickDropper() {
        super("quickDropper", ModEntities.QUICK_DROPPER, 1);
    }

    @Override
    public void tick() {
        if (!world.isRemote) {
            updateRedstone();
            if (this.getRedstoneSignal() == 0 && world.isAirBlock(pos.down())
                    && !this.getInventory().getStackInSlot(0).isEmpty()) {
                EntityItem item = new EntityItem(world, pos.down().getX() + 0.5f, pos.down().getY() + 0.5f,
                        pos.down().getZ() + 0.5f, this.getInventory().getStackInSlot(0));
                item.motionY = 0;
                item.motionX = 0;
                item.motionZ = 0;
                world.spawnEntity(item);
                this.getInventory().setStackInSlot(0, ItemStack.EMPTY);
            }
            this.markDirty();
        }
    }
}
