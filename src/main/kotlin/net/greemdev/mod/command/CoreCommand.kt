package net.greemdev.mod.command

import net.greemdev.mod.command.api.ModCommand

class CoreCommand : ModCommand("core") {
    init {
        literal {
            requires { hasPermission(2) }
            then(
                NbtCommand(),
                GamemodeCommand(),
                EnchantCommand()
            )
        }
    }
}