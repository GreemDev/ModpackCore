package net.greemdev.mod.command

import com.mojang.brigadier.context.CommandContext
import net.greemdev.mod.GreemMod
import net.greemdev.mod.network.packet.NbtDisplayPacket
import net.greemdev.mod.util.*
import net.minecraft.ChatFormatting
import net.minecraft.commands.CommandSourceStack
import net.minecraft.commands.arguments.EntityArgument
import net.minecraft.commands.arguments.coordinates.BlockPosArgument
import net.minecraft.nbt.CompoundTag
import net.minecraft.network.chat.Component
import net.minecraft.network.chat.TranslatableComponent
import net.minecraft.world.entity.EntitySelector
import net.minecraftforge.network.NetworkDirection




class NbtCommand : ModCommand("dnbt") {
    override fun CommandBuilder.modifyLiteral() {
        thenCommand("block") {
            thenArgument("pos", BlockPosArgument.blockPos()) {
                runs(::onBlock)
            }
        }
        thenCommand("entity") {
            thenArgument("target", EntityArgument.entity()) {
                runs(::onEntity)
            }
        }
        thenCommand("item") {
            runs(::onItem)
        }
    }

    private fun sendNbtScreenPacket(ctx: CommandContext<CommandSourceStack>, nbt: CompoundTag, title: Component) {
        val msg = NbtDisplayPacket(nbt, title)
        val netManager = ctx.source.playerOrException.connection.connection
        GreemMod.networking().sendTo(msg, netManager, NetworkDirection.PLAY_TO_CLIENT)
    }

    fun onBlock(ctx: CommandContext<CommandSourceStack>): Boolean {
        val pos = BlockPosArgument.getLoadedBlockPos(ctx, "pos")
        val target = ctx.source.level.getBlockEntity(pos)
        return if (target != null) {
            sendNbtScreenPacket(ctx, target.serializeNBT(),
                TranslatableComponent(ctx.source.level.getBlockState(pos).block.descriptionId).withStyle(ChatFormatting.DARK_AQUA))
            true
        } else {
            ctx.styledFailure("Target is not a block entity.")
        }
    }

    fun onEntity(ctx: CommandContext<CommandSourceStack>): Boolean {
        val target = EntityArgument.getEntity(ctx, "target")
        sendNbtScreenPacket(ctx, target.serializeNBT(), target.displayName)
        return true
    }

    fun onItem(ctx: CommandContext<CommandSourceStack>): Boolean {
        val stack = ctx.source.playerOrException.mainHandItem
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