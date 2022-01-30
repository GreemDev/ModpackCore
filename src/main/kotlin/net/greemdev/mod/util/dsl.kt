package net.greemdev.mod.util

import com.electronwill.nightconfig.core.ConfigFormat
import com.electronwill.nightconfig.core.file.CommentedFileConfig
import com.electronwill.nightconfig.core.file.CommentedFileConfigBuilder
import com.electronwill.nightconfig.core.file.FileConfig
import com.electronwill.nightconfig.core.file.FileConfigBuilder
import com.google.gson.JsonObject
import com.mojang.brigadier.arguments.ArgumentType
import com.mojang.brigadier.builder.LiteralArgumentBuilder
import com.mojang.brigadier.builder.RequiredArgumentBuilder
import net.minecraft.ChatFormatting
import net.minecraft.commands.Commands
import net.minecraft.commands.CommandSourceStack
import net.minecraft.network.chat.MutableComponent
import net.minecraft.network.chat.TextComponent
import net.minecraft.world.item.Item

fun itemProperties(func: Item.Properties.() -> Unit): Item.Properties
    = Item.Properties().apply(func)
fun textComponent(value: String, func: TextComponent.() -> Unit = {}): TextComponent
    = TextComponent(value).apply(func)
fun fileConfig(filePath: String,  func: FileConfigBuilder.() -> Unit): FileConfig
    = FileConfig.builder(filePath).apply(func).build()
fun MutableComponent.text(func: TextComponent.() -> Unit): MutableComponent
    = append(textComponent("", func))
fun MutableComponent.text(content: String, vararg formatting: ChatFormatting): MutableComponent
        = text(content) { withStyle(*formatting) }
fun MutableComponent.text(initial: Any, func: TextComponent.() -> Unit): MutableComponent
    = append(textComponent("$initial", func))
fun jsonObject(func: JsonObject.() -> Unit): JsonObject = JsonObject().apply(func)
fun JsonObject.add(name: String, func: JsonObject.() -> Unit) = add(name, jsonObject(func))
