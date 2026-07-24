package com.xray.mixin;

import com.xray.XrayMod;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockBehaviour;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BlockBehaviour.BlockStateBase.class)
public abstract class MixinAbstractBlockState {

    @Shadow
    public abstract Block getBlock();

    @Inject(method = "getRenderShape", at = @At("HEAD"), cancellable = true)
    private void xray$getRenderShape(CallbackInfoReturnable<RenderShape> cir) {
        if (xrayHide()) {
            cir.setReturnValue(RenderShape.INVISIBLE);
        }
    }

    @Inject(method = "canOcclude", at = @At("HEAD"), cancellable = true)
    private void xray$canOcclude(CallbackInfoReturnable<Boolean> cir) {
        if (xrayHide()) {
            cir.setReturnValue(false);
        }
    }

    @Inject(method = "isSolidRender", at = @At("HEAD"), cancellable = true)
    private void xray$isSolidRender(CallbackInfoReturnable<Boolean> cir) {
        if (xrayHide()) {
            cir.setReturnValue(false);
        }
    }

    @Inject(method = "skipRendering", at = @At("HEAD"), cancellable = true)
    private void xray$skipRendering(net.minecraft.world.level.block.state.BlockState neighbor,
                                     net.minecraft.core.Direction direction,
                                     CallbackInfoReturnable<Boolean> cir) {
        if (XrayMod.xrayEnabled && XrayMod.oreBlocks != null && XrayMod.oreBlocks.contains(getBlock())) {
            cir.setReturnValue(false);
        }
    }

    private boolean xrayHide() {
        return XrayMod.xrayEnabled
            && XrayMod.oreBlocks != null
            && !XrayMod.oreBlocks.contains(getBlock());
    }
}
