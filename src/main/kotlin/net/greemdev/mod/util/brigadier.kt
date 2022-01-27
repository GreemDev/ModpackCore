package net.greemdev.mod.util

import com.mojang.brigadier.Command
import com.mojang.brigadier.arguments.ArgumentType
import com.mojang.brigadier.builder.LiteralArgumentBuilder
import com.mojang.brigadier.builder.RequiredArgumentBuilder
import net.minecraft.commands.Commands
import com.mojang.brigadier.context.CommandContext
import net.minecraft.ChatFormatting.*
import net.minecraft.commands.CommandSourceStack
import net.minecraft.network.chat.TextComponent

fun TextComponent.withErrorStyle() {
    withStyle(DARK_RED)
}

fun CommandContext<CommandSourceStack>.success(content: String, builder: TextComponent.() -> Unit, sendToOps: Boolean = false): Boolean {
    source.sendSuccess(textComponent(content, builder), sendToOps)
    return true
}
fun CommandContext<CommandSourceStack>.success(component: TextComponent, sendToOps: Boolean = false): Boolean {
    source.sendSuccess(component, sendToOps)
    return true
}
fun CommandContext<CommandSourceStack>.failure(content: String, builder: TextComponent.() -> Unit): Boolean {
    source.sendFailure(textComponent(content, builder))
    return false
}
fun CommandContext<CommandSourceStack>.failure(component: TextComponent): Boolean {
    source.sendFailure(component)
    return true
}
fun CommandContext<CommandSourceStack>.styledFailure(content: String, builder: TextComponent.() -> Unit = {}): Boolean {
    source.sendFailure(textComponent(content, builder).apply { withErrorStyle() })
    return false
}

inline infix fun <reified T> CommandContext<CommandSourceStack>.argument(name: String): T
        = getArgument(name, T::class.java)

typealias CommandBuilder = BrigadierBuilder<LiteralArgumentBuilder<CommandSourceStack>>
typealias ArgumentBuilder<T> = BrigadierBuilder<RequiredArgumentBuilder<CommandSourceStack, T>>
typealias BArgBuilder<S, T> = com.mojang.brigadier.builder.ArgumentBuilder<S, T>

data class BrigadierBuilder<T : BArgBuilder<CommandSourceStack, T>>(val builder: T) {
    infix fun requires(predicate: CommandSourceStack.() -> Boolean) {
        this.builder.requires(predicate)
    }
    infix fun then(builder: LiteralArgumentBuilder<CommandSourceStack>) {
        this.builder.then(builder)
    }
    fun thenCommand(name: String, builder: CommandBuilder.() -> Unit = {}) {
        this then command(name, builder)
    }
    infix fun<T> then(builder: RequiredArgumentBuilder<CommandSourceStack, T>) {
        this.builder.then(builder)
    }
    fun<T> thenArgument(name: String, argType: ArgumentType<T>, builder: ArgumentBuilder<T>.() -> Unit = {}) {
        this then BrigadierBuilder(Commands.argument(name, argType)).apply(builder).builder
    }
    infix fun executes(command: (CommandContext<CommandSourceStack>) -> Int) {
        this.builder.executes(command)
    }
    infix fun executes(command: Command<CommandSourceStack>) {
        this.builder.executes(command)
    }
    infix fun runs(command: (CommandContext<CommandSourceStack>) -> Boolean) {
        this.builder.executes { ctx -> if (command(ctx)) 1 else 0 }
    }
}
