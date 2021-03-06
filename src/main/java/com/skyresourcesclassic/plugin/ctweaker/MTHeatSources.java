package com.skyresourcesclassic.plugin.ctweaker;

import com.skyresourcesclassic.base.HeatSources;
import crafttweaker.CraftTweakerAPI;
import crafttweaker.IAction;
import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.item.IItemStack;
import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

import java.util.HashMap;
import java.util.Map;

@ZenClass("mods.skyresourcesclassic.heatsources")
@ZenRegister
public class MTHeatSources {

    @ZenMethod
    public static void add(IItemStack stack, int heatValue) {
        if (!(CraftTweakerPlugin.toStack(stack).getItem() instanceof ItemBlock)) {
            CraftTweakerAPI.logError("Input block is not block. Did not add source.");
            return;
        }
        addRecipe(CraftTweakerPlugin.toStack(stack), heatValue);
    }

    @ZenMethod
    public static void remove(IItemStack stack) {
        if (!(CraftTweakerPlugin.toStack(stack).getItem() instanceof ItemBlock)) {
            CraftTweakerAPI.logError("Input block is not block. Did not remove source.");
            return;
        }
        removeRecipe(CraftTweakerPlugin.toStack(stack));
    }

    public static void addRecipe(ItemStack stack, int val) {
        CraftTweakerAPI.apply(new AddHeatSource(stack, val));
    }

    public static void removeRecipe(ItemStack stack) {
        CraftTweakerAPI.apply(new RemoveHeatSource(stack));
    }

    private static class AddHeatSource implements IAction {
        private final ItemStack stack;
        private final int val;

        public AddHeatSource(ItemStack stack, int val) {
            this.stack = stack;
            this.val = val;
        }

        @Override
        public void apply() {
            Block block = Block.getBlockFromItem(stack.getItem());
            HeatSources.addCTHeatSource(block.getDefaultState(), val);
        }

        @Override
        public String describe() {
            return "Adding Heat Source value for " + stack.getDisplayName();
        }
    }

    public static class RemoveHeatSource implements IAction {
        private final ItemStack stack;
        Map<ItemStack, Integer> removed = new HashMap<ItemStack, Integer>();

        public RemoveHeatSource(ItemStack stack) {
            this.stack = stack;
        }

        @Override
        public void apply() {
            Block block = Block.getBlockFromItem(stack.getItem());
            HeatSources.removeCTHeatSource(block.getDefaultState());
        }

        @Override
        public String describe() {
            return "Removing Heat Source value for " + stack.getDisplayName();
        }
    }

}
