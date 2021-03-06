package com.skyresourcesclassic.events;

import net.minecraft.block.Block;
import net.minecraft.block.BlockFlowingFluid;
import net.minecraft.fluid.Fluid;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.FillBucketEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.HashMap;
import java.util.Map;

public class ModBucketHandler {
    public Map<Fluid, Item> buckets = new HashMap<>();

    @SubscribeEvent
    public void onBucketFill(FillBucketEvent event) {
        ItemStack result = fillCustomBucket(event.getWorld(),
                event.getTarget());

        if (result == null)
            return;

        event.setFilledBucket(result);
        event.setResult(Event.Result.ALLOW);
    }

    private ItemStack fillCustomBucket(World world, RayTraceResult pos) {
        if (pos != null && pos.getBlockPos() != null
                && world.getBlockState(pos.getBlockPos()) != null) {
            Block block = world.getBlockState(pos.getBlockPos()).getBlock();
            if (block instanceof BlockFlowingFluid) {
                Fluid fluid = world.getFluidState(pos.getBlockPos()).getFluid();
                if (buckets.containsKey(fluid)) {
                    Item bucket = buckets.get(fluid);
                    if (bucket != null) {
                        world.removeBlock(pos.getBlockPos());
                        return new ItemStack(bucket, 1);
                    }
                }
            }
        }
        return null;
    }
}