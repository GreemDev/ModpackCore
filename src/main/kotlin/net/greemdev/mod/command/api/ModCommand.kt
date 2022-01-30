package net.greemdev.mod.command.api

import com.mojang.brigadier.Command
import com.mojang.brigadier.CommandDispatcher
import com.mojang.brigadier.context.CommandContext
import net.greemdev.mod.util.invoking
import net.minecraft.commands.CommandSourceStack

abstract class ModCommand(name: String) {
    protected fun literal(modifier: CommandBuilder.() -> Unit) {
        pliteral = CommandBuilder(literal).apply(modifier).builder
    }
    val literal by invoking { pliteral }
    private var pliteral = command(name)
    fun register(dispatcher: CommandDispatcher<CommandSourceStack>) {
        dispatcher.register(literal)
    }
    protected fun run(func: (MinecraftCommandContext) -> Boolean): Command<CommandSourceStack> {
        return Command { if (func(it)) 1 else 0 }
    }
}