package net.greemdev.mod.command

import com.mojang.brigadier.arguments.BoolArgumentType
import com.mojang.brigadier.arguments.IntegerArgumentType
import com.mojang.brigadier.context.CommandContext
import net.greemdev.mod.util.*
import net.minecraft.ChatFormatting
import net.minecraft.commands.CommandSourceStack
import net.minecraft.commands.arguments.EntityArgument
import net.minecraft.commands.arguments.ItemEnchantmentArgument
import net.minecraft.network.chat.TranslatableComponent
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.enchantment.EnchantmentHelper

class EnchantCommand : ModCommand("enchant") {
    override fun CommandBuilder.modifyLiteral() {
        thenArgument("targets", EntityArgument.entities()) {
            thenCommand("resetRepairCost") {
                runs(::handleResetRepairCost)
            }
            thenArgument("enchantment", ItemEnchantmentArgument.enchantment()) {
                thenArgument("overwrite", BoolArgumentType.bool()) {
                    executes { ctx -> handle(1, ctx.argument("overwrite")).run(ctx) }
                }
                thenArgument("level", IntegerArgumentType.integer(0)) {
                    executes { ctx -> handle(ctx.argument("level"), true).run(ctx) }
                    thenArgument("overwrite", BoolArgumentType.bool()) {
                        executes { ctx -> handle(ctx.argument("level"), ctx.argument("overwrite")).run(ctx) }
                    }
                }
            }
        }
    }

    private fun effectiveHeldItem(entity: LivingEntity): ItemStack = if (entity.mainHandItem.isEmpty)
        entity.offhandItem
    else entity.mainHandItem

    fun handleResetRepairCost(ctx: CommandContext<CommandSourceStack>): Boolean {
        val targets = EntityArgument.getEntities(ctx, "targets")

        var i = 0

        targets.filterIsInstance<LivingEntity>().forEach {
            val stack = effectiveHeldItem(it)
            stack.setRepairCost(0)
            i++
        }

        return if (i == 0) {
            ctx.styledFailure("No item had its repair cost reset. What happened?")
        } else {
            if (targets.sizeIsExactly(1)) {
                ctx.success("You've reset the anvil repair cost on your currently held item.", {
                        withStyle(ChatFormatting.GREEN)
                }, true)
            } else {
                ctx.success("You've reset the anvil repair cost on ${"people".quantify(i)} currently held item.", {
                        withStyle(ChatFormatting.GREEN)
                }, true)
            }
        }
    }

    fun handle(level: Int, overwriteEnchantments: Boolean) = run { ctx ->
        val targets = EntityArgument.getEntities(ctx, "targets")
        val enchantment = ItemEnchantmentArgument.getEnchantment(ctx, "enchantment")

        var i = 0

        targets.filterIsInstance<LivingEntity>().forEach {
            val stack = effectiveHeldItem(it)
            if (!stack.isEmpty) {
                val ench = EnchantmentHelper.getEnchantments(stack)
                if (!stack.isEnchanted || !ench.containsKey(enchantment)) {
                    stack.enchant(enchantment, level)
                } else if (overwriteEnchantments) {
                    if (ench.containsKey(enchantment)) {
                        ench[enchantment] = level
                        EnchantmentHelper.setEnchantments(ench, stack)
                    }
                } else stack.enchant(enchantment, level)
                i++
            } else if (targets.sizeIsExactly(1)) {
                return@run ctx.styledFailure("Cannot enchant an empty hand.")
            }
        }

        return@run if (i == 0) {
            ctx.styledFailure("No enchanting managed to occur. What happened?")
        } else {
            if (targets.size == 1) {
                ctx.success("You've applied ", {
                    withStyle(ChatFormatting.GRAY)
                    append(TranslatableComponent(enchantment.descriptionId).apply {
                        append(" ${MixinUtil.romanFromArabic(level)}")
                        withStyle(if (enchantment.isCurse) ChatFormatting.RED else ChatFormatting.GOLD)
                    })
                    withStyle(ChatFormatting.RESET)
                    append(textComponent(" on your currently held item.") {
                        withStyle(ChatFormatting.GRAY)
                    })
                }, true)
            } else {
                ctx.success("You've applied ", {
                    withStyle(ChatFormatting.GRAY)
                    append(TranslatableComponent(enchantment.descriptionId).apply {
                        append(" ${MixinUtil.romanFromArabic(level)}")
                        withStyle(if (enchantment.isCurse) ChatFormatting.RED else ChatFormatting.GOLD)
                    })
                    withStyle(ChatFormatting.RESET)
                    append(textComponent(" on ${"people".quantify(i)} currently held item.") {
                        withStyle(ChatFormatting.GRAY)
                    })
                }, true)
            }
        }
    }
}