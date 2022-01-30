package net.greemdev.mod.mixin;

import net.greemdev.mod.GreemMod;
import net.greemdev.mod.util.MixinUtil;
import net.minecraft.world.item.enchantment.MendingEnchantment;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(MendingEnchantment.class)
public abstract class MendingEnchantmentMixin {

    /**
     * @author GreemDev
     * @reason Add mending to enchantment tables, but make it rare.
     */
    @Overwrite
    public boolean isTreasureOnly() {
        if (GreemMod.config().get("enchanting.mending.obtainableInEnchantmentTable").getAsBoolean()) {
            return MixinUtil.rng(1, GreemMod.config().get("enchanting.mending.chance").getAsInt());
        } else return true;
    }
}
