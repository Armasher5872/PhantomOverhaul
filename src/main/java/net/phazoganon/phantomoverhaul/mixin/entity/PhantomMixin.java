package net.phazoganon.phantomoverhaul.mixin.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.FlyingMob;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.monster.Phantom;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Phantom.class)
public abstract class PhantomMixin extends FlyingMob implements Enemy {
    @Shadow BlockPos anchorPoint;
    protected PhantomMixin(EntityType<? extends FlyingMob> p_20806_, Level p_20807_) {
        super(p_20806_, p_20807_);
    }
    @Inject(method = "shouldDespawnInPeaceful", at = @At(value = "RETURN"), cancellable = true)
    private void shouldDespawnInPeacefulChange(CallbackInfoReturnable<Boolean> cir) {
        cir.setReturnValue(false);
    }
    @Override
    public boolean canAttack(LivingEntity livingEntity) {
        MinecraftServer minecraftServer = livingEntity.getServer();
        if (minecraftServer != null) {
            if (super.canAttack(livingEntity)) {
                this.anchorPoint = livingEntity.blockPosition().above(20 + this.random.nextInt(20));
                if (this.anchorPoint.getY() < this.level().getSeaLevel()) {
                    this.anchorPoint = new BlockPos(this.anchorPoint.getX(), this.level().getSeaLevel()+1, this.anchorPoint.getZ());
                }
            }
            return false;
        }
        else {
            return super.canAttack(livingEntity);
        }
    }
}