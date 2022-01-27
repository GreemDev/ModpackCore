package net.greemdev.mod.mixin;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Mth;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(EnchantmentHelper.class)
public abstract class EnchantmentHelperMixin {
    /**
     * @author GreemDev
     * @reason Increased Enchantment level cap to show off up to 3999 in Roman numerals.
     */
    @Overwrite
    public static int getEnchantmentLevel(CompoundTag tag) {
        return Mth.clamp(tag.getInt("lvl"), 0, 3999);
    }
}
