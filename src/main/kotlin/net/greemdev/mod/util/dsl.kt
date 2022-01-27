package net.greemdev.mod.util

import com.mojang.brigadier.arguments.ArgumentType
import com.mojang.brigadier.builder.LiteralArgumentBuilder
import com.mojang.brigadier.builder.RequiredArgumentBuilder
import net.minecraft.commands.Commands
import net.minecraft.commands.CommandSourceStack
import net.minecraft.network.chat.TextComponent
import net.minecraft.world.item.Item

fun itemProperties(func: Item.Properties.() -> Unit): Item.Properties = Item.Properties().apply(func)

fun textComponent(value: String, func: TextComponent.() -> Unit = {}): TextComponent = TextComponent(value).apply(func)

fun command(name: String, builder: CommandBuilder.() -> Unit = {}): LiteralArgumentBuilder<CommandSourceStack> {
    return CommandBuilder(Commands.literal(name)).apply(builder).builder
}

fun<T> requiredArgument(name: String, argumentType: ArgumentType<T>, builder: ArgumentBuilder<T>.() -> Unit = {}): RequiredArgumentBuilder<CommandSourceStack, T> {
    return ArgumentBuilder<T>(Commands.argument(name, argumentType)).apply(builder).builder
}