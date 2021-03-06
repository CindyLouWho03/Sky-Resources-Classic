package com.skyresourcesclassic.alchemy.block;

import com.skyresourcesclassic.alchemy.tile.CrystallizerTile;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.world.IBlockReader;

public class CrystallizerBlock extends BlockContainer {
    public CrystallizerBlock(String material, float hardness, float resistance, int tier) {
        super(Block.Properties.create(Material.ROCK).hardnessAndResistance(hardness, resistance));
        this.setRegistryName(material + "_crystallizer");
        this.tier = tier;
    }

    private int tier;

    @Override
    public EnumBlockRenderType getRenderType(IBlockState state) {
        return EnumBlockRenderType.MODEL;
    }

    @Override
    public TileEntity createNewTileEntity(IBlockReader reader) {
        return new CrystallizerTile(tier);
    }
}
