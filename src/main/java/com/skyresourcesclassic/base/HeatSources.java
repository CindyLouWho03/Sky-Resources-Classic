package com.skyresourcesclassic.base;

import net.minecraft.block.state.IBlockState;
import net.minecraft.fluid.IFluidState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class HeatSources {
    private static HashMap<IBlockState, Integer> validHeatSources;
    private static HashMap<IFluidState, Integer> validFluidHeatSources;


    public HeatSources() {
        validHeatSources = new HashMap<>();
        validFluidHeatSources = new HashMap<>();
        ctAdded = new HashMap<>();
        ctRemoved = new ArrayList<>();
    }

    public static void addHeatSource(IBlockState blockState, int value) {
        validHeatSources.put(blockState, value);
    }

    public static void addHeatSource(IFluidState blockState, int value) {
        validFluidHeatSources.put(blockState, value);
    }

    public static boolean isValidHeatSource(BlockPos pos, World world) {
        if (!validHeatSources.containsKey(world.getBlockState(pos))) {
            for (IBlockState key : validHeatSources.keySet()) {
                IBlockState block = key;
                if (block.getBlock() == world.getBlockState(pos).getBlock())
                    return true;
            }

            TileEntity tile = world.getTileEntity(pos);
            if (tile != null && tile instanceof IHeatSource)
                return true;

        } else
            return true;
        return false;
    }

    private static boolean isValidHeatSource(IBlockState state) {
        if (!validHeatSources.containsKey(state)) {
            for (IBlockState key : validHeatSources.keySet()) {
                IBlockState block = key;
                if (block.getBlock() == state.getBlock())
                    return true;
            }

        } else
            return true;

        return false;
    }

    private static boolean isValidHeatSource(IFluidState state) {
        if (!validHeatSources.containsKey(state)) {
            for (IFluidState key : validFluidHeatSources.keySet()) {
                IFluidState block = key;
                if (block.getFluid() == state.getFluid())
                    return true;
            }

        } else
            return true;

        return false;
    }

    public static int getHeatSourceValue(BlockPos pos, World world) {
        IBlockState state = world.getBlockState(pos);
        if (!isValidHeatSource(pos, world))
            return 0;

        if (validHeatSources.containsKey(state))
            return validHeatSources.get(state);
        else {
            for (IBlockState key : validHeatSources.keySet()) {
                IBlockState block = key;
                int val = validHeatSources.get(key);
                if (block.getBlock() == state.getBlock())
                    return val;
            }

            TileEntity tile = world.getTileEntity(pos);
            if (tile != null && tile instanceof IHeatSource) {
                IHeatSource source = (IHeatSource) tile;
                return source.getHeatValue();
            }
        }
        return 0;
    }

    public static int getHeatSourceValue(IBlockState state) {
        if (!isValidHeatSource(state))
            return 0;

        if (validHeatSources.containsKey(state))
            return validHeatSources.get(state);
        else {
            for (IBlockState key : validHeatSources.keySet()) {
                IBlockState block = key;
                int val = validHeatSources.get(key);
                if (block.getBlock() == state.getBlock())
                    return val;
            }
        }
        return 0;
    }

    public static int getHeatSourceValue(IFluidState state) {
        if (!isValidHeatSource(state))
            return 0;

        if (validHeatSources.containsKey(state))
            return validHeatSources.get(state);
        else {
            for (IFluidState key : validFluidHeatSources.keySet()) {
                IFluidState block = key;
                int val = validHeatSources.get(key);
                if (block.getFluid() == state.getFluid())
                    return val;
            }
        }
        return 0;
    }

    private static List<IBlockState> ctRemoved;
    private static List<IFluidState> ctFluidRemoved;
    private static HashMap<IBlockState, Integer> ctAdded;
    private static HashMap<IFluidState, Integer> ctFluidAdded;

    public static void removeCTHeatSource(IBlockState blockState) {
        ctRemoved.add(blockState);
    }

    public static void removeCTHeatSource(IFluidState fluidState) {
        ctFluidRemoved.add(fluidState);
    }

    public static void addCTHeatSource(IBlockState blockState, int value) {
        ctAdded.put(blockState, value);
    }

    public static void addCTHeatSource(IFluidState fluidState, int value) {
        ctFluidAdded.put(fluidState, value);
    }

    public static void ctRecipes() {
        for (IBlockState s : ctRemoved)
            removeHeatSource(s);
        for (IFluidState s : ctFluidRemoved)
            removeHeatSource(s);
        for (IBlockState s : ctAdded.keySet())
            addHeatSource(s, ctAdded.get(s));
        for (IFluidState s : ctFluidAdded.keySet())
            addHeatSource(s, ctFluidAdded.get(s));
    }

    private static void removeHeatSource(IBlockState blockState) {
        validHeatSources.remove(blockState);
    }

    private static void removeHeatSource(IFluidState blockState) {
        validFluidHeatSources.remove(blockState);
    }

    public static HashMap<IBlockState, Integer> getHeatSources() {
        return validHeatSources;
    }

    public static HashMap<IFluidState, Integer> getFluidHeatSources() {
        return validFluidHeatSources;
    }
}
