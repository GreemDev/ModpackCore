package net.greemdev.mod.mixin;

import net.greemdev.mod.util.MixinUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.item.enchantment.Enchantment;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

@Mixin(Enchantment.class)
public abstract class EnchantmentMixin {


    @Shadow public abstract String getDescriptionId();

    @Shadow public abstract boolean isCurse();

    @Shadow public abstract int getMaxLevel();

    /**
     * @author GreemDev
     * @reason Proper Roman Numeral representation past X; up to 3999, the Roman limit.
     */
    @Overwrite
    public Component getFullname(int level) {
        var component = new TranslatableComponent(this.getDescriptionId());
        component.withStyle(isCurse() ? ChatFormatting.RED : ChatFormatting.GRAY);
        if (level != 1 || this.getMaxLevel() != 1) {
            var roman = getRomanFromArabic(level);
            component.append(" ").append(roman != null ? roman : level + "");
        }
        return component;
    }

    public String getRomanFromArabic(int arabic) {
        return MixinUtil.romanFromArabic(arabic);
    }

}
