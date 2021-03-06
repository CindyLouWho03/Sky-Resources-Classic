package com.skyresourcesclassic.alchemy.tile;

import com.skyresourcesclassic.alchemy.item.ItemHealthGem;
import com.skyresourcesclassic.alchemy.item.ItemInfusionStone;
import com.skyresourcesclassic.base.gui.ItemHandlerSpecial;
import com.skyresourcesclassic.base.tile.TileItemInventory;
import com.skyresourcesclassic.recipe.ProcessRecipe;
import com.skyresourcesclassic.recipe.ProcessRecipeManager;
import com.skyresourcesclassic.registry.ModBlocks;
import com.skyresourcesclassic.registry.ModEntities;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.init.Particles;
import net.minecraft.item.ItemStack;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.ITickable;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;

import java.util.ArrayList;
import java.util.Arrays;

public class LifeInfuserTile extends TileItemInventory implements ITickable {
    public LifeInfuserTile() {
        super("lifeInfuser", ModEntities.LIFE_INFUSER, 3, null, new Integer[]{1, 2});
        this.setInventory(new ItemHandlerSpecial(3, null, new Integer[]{1, 2}) {
            protected void onContentsChanged(int slot) {
                super.onContentsChanged(slot);
                LifeInfuserTile.this.markDirty();
            }

            public boolean isItemValid(int slot, ItemStack stack) {
                if (slot == 0)
                    return stack.getItem() instanceof ItemHealthGem;
                else if (slot == 1)
                    return stack.getItem() instanceof ItemInfusionStone;
                return super.isItemValid(slot, stack);
            }
        });
    }

    @Override
    public void tick() {
        if (receivedPulse() && hasValidMultiblock()) {
            craftItem();
        }

        updateRedstone();
    }

    public boolean hasValidMultiblock() {
        BlockPos[] pillarPoses = new BlockPos[]{pos.north(1).west(1), pos.north(1).east(1), pos.south(1).west(1),
                pos.south(1).east(1)};
        for (BlockPos pos : pillarPoses) {
            ItemStack stack = new ItemStack(world.getBlockState(pos).getBlock());
            if (!isTag(stack, "logWood"))
                return false;
            stack = new ItemStack(world.getBlockState(pos.down()).getBlock());
            if (!isTag(stack, "logWood"))
                return false;
        }
        for (int x = -1; x < 2; x++) {
            for (int z = -1; z < 2; z++) {
                BlockPos posCheck = new BlockPos(x, 0, z).add(pos);
                ItemStack stack = new ItemStack(world.getBlockState(posCheck.up()).getBlock());
                if (x == 0 && z == 0) {
                    if (world.getBlockState(posCheck.up()).getBlock() != ModBlocks.darkMatterBlock)
                        return false;
                } else if (!isTag(stack, "treeLeaves"))
                    return false;

            }
        }
        return true;
    }

    private boolean isTag(ItemStack stack, String entry) {
        if (stack == ItemStack.EMPTY || stack.getItem() == null)
            return false;

        return new ItemTags.Wrapper(new ResourceLocation(entry)).getAllElements().contains(stack.getItem());
    }

    private void craftItem() {
        ProcessRecipe recipe = recipeToCraft();
        boolean worked = false;
        if (recipe != null) {
            if (!world.isRemote) {
                getInventory().extractItem(2, ((ItemStack) recipe.getInputs().get(0)).getCount(), false);
                getInventory().getStackInSlot(1).setDamage(getInventory().getStackInSlot(1).getDamage() + 1);
                if (getInventory().getStackInSlot(1).getDamage() >= getInventory().getStackInSlot(1).getMaxDamage())
                    getInventory().setStackInSlot(1, ItemStack.EMPTY);
                world.removeBlock(pos.down(1));
                ItemStack gemStack = getInventory().getStackInSlot(0);
                if (gemStack != ItemStack.EMPTY && gemStack.getItem() instanceof ItemHealthGem) {
                    ItemHealthGem healthGem = (ItemHealthGem) gemStack.getItem();
                    gemStack.getTag().setInt("health",
                            gemStack.getTag().getInt("health") - (int) recipe.getIntParameter());
                }

                ItemStack stack = recipe.getOutputs().get(0).copy();

                Entity entity = new EntityItem(world, pos.getX() + 0.5F, pos.getY() - 0.5F, pos.getZ() + 0.5F, stack);
                world.spawnEntity(entity);
            }
            if (worked) {
                this.world.spawnParticle(Particles.HEART, pos.getX() + 0.5D, pos.getY() - 0.5D,
                        pos.getZ() + 0.5D, 0.0D, 0.0D, 0.0D);
            }
        }
    }

    public int getHealthInGem() {
        ItemStack gemStack = getInventory().getStackInSlot(0);
        if (gemStack != ItemStack.EMPTY && gemStack.getItem() instanceof ItemHealthGem) {
            ItemHealthGem healthGem = (ItemHealthGem) gemStack.getItem();
            return healthGem.getHealthInjected(gemStack);
        }
        return 0;
    }

    private ProcessRecipe recipeToCraft() {
        IBlockState state = this.world.getBlockState(this.pos.down(1));
        if (state != null) {
            ProcessRecipe recipe = ProcessRecipeManager.infusionRecipes
                    .getRecipe(
                            new ArrayList<Object>(Arrays.asList((Object) getInventory().getStackInSlot(2),
                                    (Object) new ItemStack(state.getBlock()))),
                            this.getHealthInGem(), false, false);

            if (recipe != null && recipe.getIntParameter() <= this.getHealthInGem()
                    && getInventory().getStackInSlot(1) != ItemStack.EMPTY
                    && getInventory().getStackInSlot(1).getItem() instanceof ItemInfusionStone)
                return recipe;
        }
        return null;
    }
}
