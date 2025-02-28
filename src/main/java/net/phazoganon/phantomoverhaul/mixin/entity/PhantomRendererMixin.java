package net.phazoganon.phantomoverhaul.mixin.entity;

import net.minecraft.client.model.PhantomModel;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.PhantomRenderer;
import net.minecraft.client.renderer.entity.state.PhantomRenderState;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.monster.Phantom;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(PhantomRenderer.class)
public abstract class PhantomRendererMixin extends MobRenderer<Phantom, PhantomRenderState, PhantomModel> {
    @Shadow public abstract ResourceLocation getTextureLocation(PhantomRenderState p_363735_);
    public PhantomRendererMixin(EntityRendererProvider.Context p_174304_, PhantomModel p_174305_, float p_174306_) {
        super(p_174304_, p_174305_, p_174306_);
    }
    @Nullable
    @Override
    protected RenderType getRenderType(PhantomRenderState renderState, boolean isVisible, boolean renderTranslucent, boolean appearsGlowing) {
        return RenderType.ENTITY_TRANSLUCENT.apply(getTextureLocation(renderState), false);
    }
}