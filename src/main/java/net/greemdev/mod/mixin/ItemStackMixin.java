package net.greemdev.mod.mixin;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraftforge.common.capabilities.CapabilityProvider;
import net.minecraftforge.common.extensions.IForgeItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import javax.annotation.Nullable;

@Mixin(ItemStack.class)
public abstract class ItemStackMixin {

    @Shadow public abstract CompoundTag getOrCreateTag();

    @Shadow private CompoundTag tag;

    private ItemStack curr() {
        return (ItemStack) (Object) this;
    }

    /**
     * @author GreemDev
     * @reason Fucky storage
     */
    @Overwrite
    public void enchant(Enchantment enchantment, int level) {
        this.getOrCreateTag();
        if (!this.tag.contains("Enchantments", 9)) {
            this.tag.put("Enchantments", new ListTag());
        }

        ListTag newTag = tag.getList("Enchantments", 10);
        newTag.add(storeEnch(EnchantmentHelper.getEnchantmentId(enchantment), level));
    }

    private static CompoundTag storeEnch(@Nullable ResourceLocation loc, int level) {
        var tag = new CompoundTag();
        tag.putString("id", String.valueOf(loc));
        tag.putInt("lvl", level);
        return tag;
    }
}
