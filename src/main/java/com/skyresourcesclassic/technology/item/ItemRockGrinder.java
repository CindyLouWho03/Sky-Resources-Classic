package com.skyresourcesclassic.technology.item;

import com.google.common.collect.Multimap;
import com.skyresourcesclassic.ConfigOptions;
import com.skyresourcesclassic.ItemHelper;
import com.skyresourcesclassic.RandomHelper;
import com.skyresourcesclassic.recipe.ProcessRecipe;
import com.skyresourcesclassic.recipe.ProcessRecipeManager;
import com.skyresourcesclassic.registry.ModItemGroups;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Enchantments;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemPickaxe;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemTier;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.ToolType;

import java.util.Collections;

public class ItemRockGrinder extends ItemPickaxe {
    private float damageVsEntity;

    private ItemTier toolMaterial;

    public ItemRockGrinder(ItemTier material, String name) {
        super(material, (int) material.getAttackDamage(), material.getAttackDamage(), new Item.Properties().addToolType(ToolType.get("rockGrinder"), material.getHarvestLevel()).defaultMaxDamage((int) (material.getMaxUses() * ConfigOptions.rockGrinder.rockGrinderBaseDurability.get())).maxStackSize(1).group(ModItemGroups.tabTech));
        toolMaterial = material;
        this.damageVsEntity = (float) (ConfigOptions.rockGrinder.rockGrinderBaseDamage.get() + material.getAttackDamage());
        setRegistryName(name);

        ItemHelper.addRockGrinder(this);
    }

    @Override
    public float getDestroySpeed(ItemStack stack, IBlockState state) {
        Block block = state.getBlock();

        ProcessRecipe rec = new ProcessRecipe(
                Collections.singletonList(new ItemStack(state.getBlock())),
                Integer.MAX_VALUE, "rockgrinder");
        for (ProcessRecipe r : ProcessRecipeManager.rockGrinderRecipes.getRecipes()) {
            if (r != null && rec.isInputRecipeEqualTo(r, false)) {
                if (toolMaterial.getHarvestLevel() < block.getHarvestLevel(state))
                    return 0.5F;
                else {
                    return toolMaterial.getEfficiency();
                }
            }
        }
        return 0.5F;

    }

    @Override
    public boolean onBlockStartBreak(ItemStack item, BlockPos pos, EntityPlayer player) {
        World world = player.world;
        IBlockState state = world.getBlockState(pos);
        if (item.attemptDamageItem(1, this.random, null)) {
            player.setHeldItem(EnumHand.MAIN_HAND, ItemStack.EMPTY);
        }

        boolean worked = false;
        ProcessRecipe rec = new ProcessRecipe(
                Collections.singletonList(new ItemStack(state.getBlock())),
                Integer.MAX_VALUE, "rockgrinder");
        for (ProcessRecipe r : ProcessRecipeManager.rockGrinderRecipes.getRecipes()) {
            if (r != null && rec.isInputRecipeEqualTo(r, false)) {
                worked = true;
                if (!world.isRemote) {
                    int level = EnchantmentHelper.getEnchantmentLevel(Enchantments.FORTUNE, item);
                    float chance = r.getIntParameter() * (((float) level + 3F) / 3F);
                    while (chance >= 1) {
                        RandomHelper.spawnItemInWorld(world, r.getOutputs().get(0).copy(), pos);
                        chance -= 1;
                    }
                    if (random.nextFloat() <= chance)
                        RandomHelper.spawnItemInWorld(world, r.getOutputs().get(0).copy(), pos);
                }
            }
        }
        world.destroyBlock(pos, !worked);
        return worked;

    }

    @Override
    public Multimap<String, AttributeModifier> getAttributeModifiers(EntityEquipmentSlot equipmentSlot) {

        Multimap<String, AttributeModifier> multimap = super.getAttributeModifiers(equipmentSlot);

        if (equipmentSlot == EntityEquipmentSlot.MAINHAND) {
            multimap.put(SharedMonsterAttributes.ATTACK_DAMAGE.getName(),
                    new AttributeModifier(ATTACK_DAMAGE_MODIFIER, "Weapon modifier", this.damageVsEntity, 0));
            multimap.put(SharedMonsterAttributes.ATTACK_SPEED.getName(),
                    new AttributeModifier(ATTACK_SPEED_MODIFIER, "Weapon modifier", -2.4000000953674316D, 0));
        }

        return multimap;
    }
}
