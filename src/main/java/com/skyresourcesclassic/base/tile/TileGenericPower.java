package com.skyresourcesclassic.base.tile;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;

public class TileGenericPower extends TileItemInventory implements IEnergyStorage {

    public TileGenericPower(String name, TileEntityType type, int maxPower, int maxIn, int maxOut) {
        super(name, type, 0);
        maxEnergy = maxPower;
        maxReceive = maxIn;
        maxExtract = maxOut;
    }

    public TileGenericPower(String name, TileEntityType type, int maxPower, int maxIn, int maxOut, int invSlots, Integer[] noInsert, Integer[] noExtract) {
        super(name, type, invSlots, noInsert, noExtract);
        maxEnergy = maxPower;
        maxReceive = maxIn;
        maxExtract = maxOut;
    }

    protected int energy;
    private int maxEnergy;
    private int maxReceive;
    private int maxExtract;

    @Override
    public int receiveEnergy(int maxReceive, boolean simulate) {
        int energyReceived = (int) Math.min(getMaxEnergyStored() - energy, Math.min(this.maxReceive, maxReceive));
        if (!simulate) {
            energy += energyReceived;
            this.markDirty();
        }
        return energyReceived;
    }

    @Override
    public int extractEnergy(int maxExtract, boolean simulate) {
        int energyExtract = (int) Math.min(energy, Math.min(this.maxExtract, maxExtract));
        if (!simulate) {
            System.out.println("HERE");
            energy -= energyExtract;
            this.markDirty();
        }
        return energyExtract;
    }

    public int internalExtractEnergy(int extract, boolean simulate) {
        int energyExtract = (int) Math.min(energy, extract);
        if (!simulate) {
            energy -= extract;
            this.markDirty();
        }
        return energyExtract;
    }

    @Override
    public int getEnergyStored() {
        return energy;
    }

    @Override
    public int getMaxEnergyStored() {
        return maxEnergy;
    }

    @Override
    public boolean canExtract() {
        return maxExtract != 0;
    }

    @Override
    public boolean canReceive() {
        return maxReceive != 0;
    }

    @Override
    public NBTTagCompound write(NBTTagCompound compound) {
        compound = super.write(compound);

        compound.setInt("Energy", energy);

        return compound;
    }

    @Override
    public void read(NBTTagCompound compound) {
        super.read(compound);
        energy = compound.getInt("Energy");
    }

    @Override
    public <T> LazyOptional<T> getCapability(Capability<T> capability, EnumFacing facing) {
        if (capability == CapabilityEnergy.ENERGY) {
            return LazyOptional.of(() -> this).cast();
        }
        return super.getCapability(capability, facing);
    }
}
