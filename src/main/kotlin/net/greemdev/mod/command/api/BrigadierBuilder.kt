package net.greemdev.mod.command.api

import com.mojang.brigadier.Command
import com.mojang.brigadier.arguments.ArgumentType
import com.mojang.brigadier.builder.LiteralArgumentBuilder
import com.mojang.brigadier.builder.RequiredArgumentBuilder
import com.mojang.brigadier.context.CommandContext
import net.minecraft.commands.CommandSourceStack
import net.minecraft.commands.Commands

typealias CommandBuilder = BrigadierBuilder<MinecraftLiteralBuilder>
typealias ArgumentBuilder<T> = BrigadierBuilder<MinecraftArgumentBuilder<T>>
private typealias BArgBuilder<S, T> = com.mojang.brigadier.builder.ArgumentBuilder<S, T>

data class BrigadierBuilder<T : BArgBuilder<CommandSourceStack, T>>(val builder: T) {
    infix fun requires(predicate: CommandSourceStack.() -> Boolean) {
        this.builder.requires(predicate)
    }
    infix fun then(builder: MinecraftLiteralBuilder) {
        this.builder.then(builder)
    }
    fun then(name: String, builder: CommandBuilder.() -> Unit = {}) {
        this then command(name, builder)
    }
    infix fun<T> then(builder: MinecraftArgumentBuilder<T>) {
        this.builder.then(builder)
    }
    fun then(vararg commands: ModCommand) {
        commands.map { it.literal }.forEach(::then)
    }
    fun<T> then(name: String, argType: ArgumentType<T>, builder: ArgumentBuilder<T>.() -> Unit = {}) {
        this then BrigadierBuilder(Commands.argument(name, argType)).apply(builder).builder
    }
    infix fun executes(command: (MinecraftCommandContext) -> Int) {
        this.builder.executes(command)
    }
    infix fun executes(command: Command<CommandSourceStack>) {
        this.builder.executes(command)
    }
    infix fun runs(command: (MinecraftCommandContext) -> Boolean) {
        this.builder.executes { ctx -> if (command(ctx)) 1 else 0 }
    }
}