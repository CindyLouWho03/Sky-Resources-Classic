package com.skyresourcesclassic.base.item;

import com.skyresourcesclassic.base.entity.EntitySurvivalistHook;
import com.skyresourcesclassic.registry.ModItemGroups;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemFishingRod;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.world.World;

public class ItemSurvivalFishingRod extends ItemFishingRod {

    public ItemSurvivalFishingRod(String name) {
        super(new Item.Properties().group(ModItemGroups.tabMain).defaultMaxDamage(32));
        setRegistryName(name);
    }

    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) {
        ItemStack itemstack = playerIn.getHeldItem(handIn);

        if (playerIn.fishEntity != null) {
            playerIn.swingArm(handIn);
        } else {
            worldIn.playSound(null, playerIn.posX, playerIn.posY, playerIn.posZ, SoundEvents.ENTITY_BOBBER_THROW, SoundCategory.NEUTRAL, 0.5F, 0.4F / (random.nextFloat() * 0.4F + 0.8F));

            if (!worldIn.isRemote) {
                EntitySurvivalistHook entityfishhook = new EntitySurvivalistHook(worldIn, playerIn);
                int j = EnchantmentHelper.getFishingSpeedBonus(itemstack);

                if (j > 0) {
                    entityfishhook.setLureSpeed(j);
                }

                int k = EnchantmentHelper.getFishingLuckBonus(itemstack);

                if (k > 0) {
                    entityfishhook.setLuck(k);
                }

                worldIn.spawnEntity(entityfishhook);
            }

            playerIn.swingArm(handIn);
            playerIn.addStat(StatList.ITEM_USED.get(this));
        }

        return new ActionResult(EnumActionResult.SUCCESS, itemstack);
    }
}
