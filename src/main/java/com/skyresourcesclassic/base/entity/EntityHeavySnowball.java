package com.skyresourcesclassic.base.entity;

import com.skyresourcesclassic.registry.ModEntities;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityBlaze;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.init.Particles;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class EntityHeavySnowball extends EntityThrowable {
    public EntityHeavySnowball(World worldIn) {
        super(ModEntities.HEAVY_SNOWBALL, worldIn);
    }

    public EntityHeavySnowball(EntityLivingBase throwerIn, World worldIn) {
        super(ModEntities.HEAVY_SNOWBALL, throwerIn, worldIn);
        this.moveRelative(1, .5f, 1, 0.1F);
    }

    public EntityHeavySnowball(double x, double y, double z, World worldIn) {
        super(ModEntities.HEAVY_SNOWBALL, x, y, z, worldIn);
    }

    @OnlyIn(Dist.CLIENT)
    public void handleStatusUpdate(byte id) {
        if (id == 3) {
            for (int i = 0; i < 8; ++i) {
                this.world.spawnParticle(Particles.ITEM_SNOWBALL, this.posX, this.posY, this.posZ, 0.0D, 0.0D, 0.0D);
            }
        }
    }

    /**
     * Called when this EntityThrowable hits a block or entity.
     */
    protected void onImpact(RayTraceResult result) {
        if (result.entity != null) {
            int i = 8;

            if (result.entity instanceof EntityBlaze) {
                i = 14;
            }

            result.entity.attackEntityFrom(DamageSource.causeThrownDamage(this, this.getThrower()), (float) i);
        }

        if (!this.world.isRemote) {
            this.remove();
        }
    }

}
