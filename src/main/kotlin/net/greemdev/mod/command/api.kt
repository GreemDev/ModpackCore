package net.greemdev.mod.command

import com.mojang.brigadier.Command
import com.mojang.brigadier.CommandDispatcher
import com.mojang.brigadier.builder.LiteralArgumentBuilder
import com.mojang.brigadier.context.CommandContext
import net.greemdev.mod.util.CommandBuilder
import net.greemdev.mod.util.command
import net.minecraft.commands.CommandSourceStack

abstract class ModCommand(name: String) {
    private val subLiterals = arrayListOf<LiteralArgumentBuilder<CommandSourceStack>>()

    protected fun addSubLiteral(builder: LiteralArgumentBuilder<CommandSourceStack>) {
        subLiterals.add(builder)
    }
    protected fun subLiterals(vararg builders: LiteralArgumentBuilder<CommandSourceStack>) {
        builders.forEach(::addSubLiteral)
    }
    protected fun addSubLiteral(command: ModCommand) {
        addSubLiteral(command.buildLiteral())
    }
    protected fun subLiterals(vararg commands: ModCommand) {
        subLiterals(*commands.map(ModCommand::buildLiteral).toTypedArray())
    }

    private val literal = command(name)
    fun register(dispatcher: CommandDispatcher<CommandSourceStack>) {
        dispatcher.register(buildLiteral().apply {
            subLiterals.forEach(::then)
        })
    }
    protected fun run(func: (CommandContext<CommandSourceStack>) -> Boolean): Command<CommandSourceStack> {
        return Command { if (func(it)) 1 else 0 }
    }
    fun buildLiteral() = CommandBuilder(literal).also { it.modifyLiteral() }.builder
    open fun CommandBuilder.modifyLiteral() {}
}