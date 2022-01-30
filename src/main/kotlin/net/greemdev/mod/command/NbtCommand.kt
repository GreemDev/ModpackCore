package net.greemdev.mod.command

import com.mojang.brigadier.context.CommandContext
import net.greemdev.mod.GreemMod
import net.greemdev.mod.command.api.*
import net.greemdev.mod.network.packet.NbtDisplayPacket
import net.greemdev.mod.util.*
import net.minecraft.ChatFormatting
import net.minecraft.commands.CommandSourceStack
import net.minecraft.commands.arguments.EntityArgument
import net.minecraft.commands.arguments.coordinates.BlockPosArgument
import net.minecraft.nbt.CompoundTag
import net.minecraft.network.chat.Component
import net.minecraft.network.chat.TranslatableComponent
import net.minecraft.world.entity.player.Player
import net.minecraftforge.network.NetworkDirection

class NbtCommand : ModCommand("nbt") {
    init {
        literal {
            then("block") {
                then("pos", arg.blockPos()) {
                    runs(::onBlock)
                }
            }
            then("entity") {
                then("target", arg.entity()) {
                    runs(::onEntity)
                }
            }
            then("item") {
                runs(::onItem)
            }
        }
    }

    private fun sendNbtScreenPacket(ctx: MinecraftCommandContext, nbt: CompoundTag, title: Component) {
        val msg = NbtDisplayPacket(nbt, title)
        val netManager = ctx.source.playerOrException.connection.connection
        GreemMod.networking().sendTo(msg, netManager, NetworkDirection.PLAY_TO_CLIENT)
    }

    private fun onBlock(ctx: MinecraftCommandContext): Boolean {
        val pos = ctx.argument("pos", BlockPosArgument::getLoadedBlockPos)
        val target = ctx.source.level.getBlockEntity(pos)
        return if (target != null) {
            sendNbtScreenPacket(ctx, target.serializeNBT(),
                TranslatableComponent(ctx.source.level.getBlockState(pos).block.descriptionId).withStyle(ChatFormatting.DARK_AQUA))
            true
        } else {
            ctx.styledFailure("Target is not a block entity.")
        }
    }

    private fun onEntity(ctx: MinecraftCommandContext): Boolean {
        val target = ctx.argument("target", EntityArgument::getEntity)
        sendNbtScreenPacket(ctx, target.serializeNBT(), target.displayName)
        return true
    }

    private fun onItem(ctx: MinecraftCommandContext): Boolean {
        val stack = ctx.player.heldItem
        return if (stack.isEmpty) {
            ctx.styledFailure("There is no item in your hand.")
        } else if (!stack.hasTag()) {
            ctx.styledFailure("The item in your hand doesn't have any attached NBT data.")
        } else {
            sendNbtScreenPacket(ctx, stack.orCreateTag, stack.hoverName)
            true
        }
    }

}