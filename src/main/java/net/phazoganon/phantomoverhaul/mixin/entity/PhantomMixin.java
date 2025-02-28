package net.phazoganon.phantomoverhaul.mixin.entity;

import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Camera;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.server.MinecraftServer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
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

    @Shadow public abstract int getUniqueFlapTickOffset();

    protected PhantomMixin(EntityType<? extends FlyingMob> p_20806_, Level p_20807_) {
        super(p_20806_, p_20807_);
    }
    @Override
    public void tick() {
        super.tick();
        if (this.level().isClientSide) {
            float f = Mth.cos((float) (this.getUniqueFlapTickOffset()+this.tickCount)*(float) Math.toRadians(7.448451F)+Mth.PI);
            float f1 = Mth.cos((float) (this.getUniqueFlapTickOffset()+this.tickCount+1)*(float) Math.toRadians(7.448451F)+Mth.PI);
            if (f > 0.0F && f1 <= 0.0F) {
                this.level().playLocalSound(this.getX(), this.getY(), this.getZ(), SoundEvents.PHANTOM_FLAP, this.getSoundSource(), 0.95F+this.random.nextFloat()*0.05F, 0.95F+this.random.nextFloat()*0.05F, false);
            }
            float f2 = this.getBbWidth()*1.48F;
            float f3 = Mth.cos((float) Math.toRadians(this.getYRot()))*f2;
            float f4 = Mth.sin((float) Math.toRadians(this.getYRot()))*f2;
            float f5 = (0.3F+f*0.45F)* this.getBbHeight()*2.5F;
            this.level().addParticle(ParticleTypes.WHITE_ASH, this.getX()+(double)f3, this.getY()+(double)f5, this.getZ()+(double)f4, 0.0, 0.0, 0.0);
            this.level().addParticle(ParticleTypes.WHITE_ASH, this.getX()-(double)f3, this.getY()+(double)f5, this.getZ()-(double)f4, 0.0, 0.0, 0.0);
        }
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