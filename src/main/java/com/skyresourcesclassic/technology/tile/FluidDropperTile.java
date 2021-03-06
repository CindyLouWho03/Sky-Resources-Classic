package com.skyresourcesclassic.technology.tile;

import com.skyresourcesclassic.ConfigOptions;
import com.skyresourcesclassic.base.tile.TileBase;
import com.skyresourcesclassic.registry.ModEntities;
import net.minecraft.init.SoundEvents;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.SoundCategory;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidTankProperties;

public class FluidDropperTile extends TileBase implements ITickable, IFluidHandler {
    private FluidTank tank;

    @Override
    public IFluidTankProperties[] getTankProperties() {
        return tank.getTankProperties();
    }

    @Override
    public int fill(FluidStack resource, boolean doFill) {
        if (resource != null) {
            int filled = tank.fill(resource, doFill);

            return filled;
        }

        return 0;
    }

    @Override
    public FluidStack drain(FluidStack resource, boolean doDrain) {
        if (resource != null) {
            return tank.drain(resource.amount, doDrain);
        }

        return null;
    }

    @Override
    public FluidStack drain(int maxDrain, boolean doDrain) {
        return tank.drain(maxDrain, doDrain);
    }

    public FluidTank getTank() {
        return tank;
    }

    public FluidDropperTile() {
        super("fluidDropper", ModEntities.FLUID_DROPPER);
        tank = new FluidTank(ConfigOptions.fluidDropper.fluidDropperCapacity.get());
    }

    @Override
    public NBTTagCompound write(NBTTagCompound compound) {
        super.write(compound);

        tank.writeToNBT(compound);
        return compound;
    }

    @Override
    public void read(NBTTagCompound compound) {
        super.read(compound);

        tank.readFromNBT(compound);
    }

    @Override
    public void tick() {
        if (!world.isRemote) {
            updateRedstone();
            pullFromAround();

            if (tank.getFluidAmount() >= 1000 && world.isAirBlock(pos.down())) {
                world.setBlockState(pos.down(), tank.getFluid().getFluid().getBlock().getDefaultState());
                tank.setFluid(null);
                world.playSound(pos.getX(), pos.getY(), pos.getZ(), SoundEvents.ITEM_BUCKET_EMPTY,
                        SoundCategory.BLOCKS, 0.5F, 1, true);
            }
        }
    }

    private void pullFromAround() {
        EnumFacing[] checkPoses = new EnumFacing[]{EnumFacing.UP, EnumFacing.UP.NORTH, EnumFacing.SOUTH, EnumFacing.WEST,
                EnumFacing.EAST};
        if (this.getRedstoneSignal() == 0) {
            for (EnumFacing dir : checkPoses) {
                TileEntity tile = world.getTileEntity(this.pos.add(dir.getDirectionVec()));
                if (tile != null && tile.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, dir).isPresent()) {
                    IFluidHandler fluidHand = (IFluidHandler) tile.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, dir);

                    int amt = this.fill(fluidHand.drain(ConfigOptions.fluidDropper.fluidDropperCapacity.get() - tank.getFluidAmount(), true),
                            true);
                    if (amt > 0)
                        return;
                }
            }
        }
    }

    @Override
    public <T> LazyOptional<T> getCapability(Capability<T> capability, EnumFacing facing) {
        if (capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY) {
            return LazyOptional.of(() -> this).cast();
        }
        return super.getCapability(capability, facing);
    }
}
