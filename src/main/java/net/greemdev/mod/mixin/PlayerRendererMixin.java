package net.greemdev.mod.mixin;

import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(PlayerRenderer.class)
public abstract class PlayerRendererMixin {

    @Redirect(method = "setupRotations(Lnet/minecraft/client/player/AbstractClientPlayer;Lcom/mojang/blaze3d/vertex/PoseStack;FFF)V", at = @At(
            value = "INVOKE",
            target = "Ljava/lang/Math;acos(D)D"
    ))
    private double fixDisappearing(double d) {
        /// from https://github.com/TheRandomLabs/RandomPatches
        //Sometimes, Math#acos(double) is called with a value larger than 1.0, which results in
        //a rotation angle of NaN, thus causing the player model to disappear.
        return Math.acos(Math.min(d, 1.0));
    }
}
