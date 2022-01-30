package net.greemdev.mod.mixin;

import net.greemdev.mod.GreemMod;
import net.greemdev.mod.util.MixinUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.item.enchantment.Enchantment;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import java.util.function.Supplier;

@Mixin(Enchantment.class)
public abstract class EnchantmentMixin {

    @Shadow
    public abstract String getDescriptionId();

    @Shadow
    public abstract boolean isCurse();

    @Shadow
    public abstract int getMaxLevel();

    /**
     * @author GreemDev
     * @reason Proper Roman Numeral representation past X; up to 3999, the Roman limit.
     */
    @Overwrite
    public Component getFullname(int level) {
        var component = new TranslatableComponent(getDescriptionId()).withStyle(getStyle(level));

        if (shouldDisplayLevel(level))
            component.append(" ").append(getRomanFromArabic(level));

        return component;
    }

    private ChatFormatting getStyle(int level) {
        Supplier<ChatFormatting> getFormattingOrDefault = () ->
                GreemMod.config().get("qol.coloredEnchantments").getAsBoolean()
                        ? getColorFromLevel(level)
                        : ChatFormatting.GRAY;

        return isCurse() ? ChatFormatting.DARK_RED : getFormattingOrDefault.get();
    }

    private boolean shouldDisplayLevel(int level) {
        return getMaxLevel() != 1 || level != 1;
    }

    private ChatFormatting getColorFromLevel(int level) {
        if (level == 1) {
            return ChatFormatting.DARK_AQUA; //level 1
        } else if (MixinUtil.isInRange(level, 2, 5)) {
            return ChatFormatting.GREEN; //levels 2, 3, 4
        } else if (level == 5) {
            return ChatFormatting.GOLD;
        } else if (MixinUtil.isInRange(level, 6, 11)) {
            return ChatFormatting.DARK_PURPLE; //levels 6-10
        } else return ChatFormatting.DARK_BLUE; //levels >10
    }

    private String getRomanFromArabic(int arabic) {
        return MixinUtil.romanFromArabic(arabic);
    }

}
