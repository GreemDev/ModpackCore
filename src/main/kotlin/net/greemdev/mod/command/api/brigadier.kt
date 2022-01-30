package net.greemdev.mod.command.api

import com.mojang.brigadier.Command
import com.mojang.brigadier.arguments.ArgumentType
import com.mojang.brigadier.builder.ArgumentBuilder
import com.mojang.brigadier.builder.LiteralArgumentBuilder
import com.mojang.brigadier.builder.RequiredArgumentBuilder
import net.minecraft.commands.Commands
import com.mojang.brigadier.context.CommandContext
import net.greemdev.mod.util.textComponent
import net.minecraft.ChatFormatting.*
import net.minecraft.commands.CommandSourceStack
import net.minecraft.network.chat.TextComponent

typealias MinecraftCommandContext = CommandContext<CommandSourceStack>
typealias MinecraftLiteralBuilder = LiteralArgumentBuilder<CommandSourceStack>
typealias MinecraftArgumentBuilder<T> = RequiredArgumentBuilder<CommandSourceStack, T>

fun command(name: String, builder: CommandBuilder.() -> Unit = {}): MinecraftLiteralBuilder {
    return CommandBuilder(Commands.literal(name)).apply(builder).builder
}

fun TextComponent.withErrorStyle() {
    withStyle(DARK_RED)
}

val MinecraftCommandContext.player
    get() = source.playerOrException

val MinecraftCommandContext.entity
    get() = source.entityOrException

fun MinecraftCommandContext.success(content: String, builder: TextComponent.() -> Unit, sendToOps: Boolean = false): Boolean {
    source.sendSuccess(textComponent(content, builder), sendToOps)
    return true
}
fun MinecraftCommandContext.success(component: TextComponent, sendToOps: Boolean = false): Boolean {
    source.sendSuccess(component, sendToOps)
    return true
}
fun MinecraftCommandContext.failure(content: String, builder: TextComponent.() -> Unit): Boolean {
    source.sendFailure(textComponent(content, builder))
    return false
}
fun MinecraftCommandContext.failure(component: TextComponent): Boolean {
    source.sendFailure(component)
    return true
}
fun MinecraftCommandContext.styledFailure(content: String, builder: TextComponent.() -> Unit = {}): Boolean {
    source.sendFailure(textComponent(content, builder).apply { withErrorStyle() })
    return false
}
