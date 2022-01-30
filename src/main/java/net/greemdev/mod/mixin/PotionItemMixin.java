package net.greemdev.mod.mixin;

import net.greemdev.mod.GreemMod;
import net.minecraft.world.item.PotionItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PotionItem.class)
public abstract class PotionItemMixin {
    @Inject(method = "isFoil", at = @At("HEAD"), cancellable = true)
    private void removePotionItemEnchantGlint(CallbackInfoReturnable<Boolean> cir) {
        cir.setReturnValue(!GreemMod.config().get("qol.removePotionEnchantGlint").getAsBoolean());
    }
}
