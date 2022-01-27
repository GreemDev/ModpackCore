package net.greemdev.mod.command

import net.greemdev.mod.util.CommandBuilder

class CoreCommand : ModCommand("core") {
    init {
        subLiterals(
            NbtCommand(),
            GamemodeCommand(),
            EnchantCommand()
        )
    }

    override fun CommandBuilder.modifyLiteral() {
        requires { hasPermission(2) }
    }
}