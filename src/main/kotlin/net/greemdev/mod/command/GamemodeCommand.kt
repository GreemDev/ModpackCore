package net.greemdev.mod.command

import net.greemdev.mod.util.CommandBuilder
import net.greemdev.mod.util.success
import net.greemdev.mod.util.textComponent
import net.minecraft.ChatFormatting
import net.minecraft.Util
import net.minecraft.commands.CommandSourceStack
import net.minecraft.commands.arguments.EntityArgument
import net.minecraft.world.level.GameRules
import net.minecraft.world.level.GameType
import net.minecraft.world.level.GameType.*

class GamemodeCommand : ModCommand("gamemode") {
    override fun CommandBuilder.modifyLiteral() {
        thenCommand("c") {
            executes(self(CREATIVE))
            thenArgument("target", EntityArgument.players()) {
                executes(others(CREATIVE))
            }
        }
        thenCommand("s") {
            executes(self(DEFAULT_MODE))
            thenArgument("target", EntityArgument.players()) {
                executes(others(DEFAULT_MODE))
            }
        }
        thenCommand("a") {
            executes(self(ADVENTURE))
            thenArgument("target", EntityArgument.players()) {
                executes(others(ADVENTURE))
            }
        }
        thenCommand("sp") {
            executes(self(SPECTATOR))
            thenArgument("target", EntityArgument.players()) {
                executes(others(SPECTATOR))
            }
        }
        GameType.values().forEach {
            thenCommand(it.getName()) {
                executes(self(it))
                thenArgument("target", EntityArgument.players()) {
                    executes(others(it))
                }
            }
        }
    }

    private fun colorFromGameMode(gamemode: GameType): ChatFormatting = if (gamemode == GameType.SURVIVAL)
        ChatFormatting.DARK_RED
    else if (gamemode == GameType.ADVENTURE)
        ChatFormatting.GOLD
    else if (gamemode.isCreative)
        ChatFormatting.AQUA
    else ChatFormatting.DARK_AQUA

    private fun gamemodeChangedText(gameType: GameType) =
        textComponent("Your gamemode has been changed to ") {
            withStyle(ChatFormatting.GRAY)
            append(textComponent(gameType.getName().replaceFirstChar(Char::uppercase)) {
                withStyle(colorFromGameMode(gameType))
            })
            append(textComponent(".") { withStyle(ChatFormatting.RESET) })
        }

    fun self(gameType: GameType) = run { ctx ->
        if (ctx.source.playerOrException.setGameMode(gameType)) {
            ctx.success(gamemodeChangedText(gameType), true)
        } else false
    }

    fun others(gameType: GameType) = run { ctx ->
        val players = EntityArgument.getPlayers(ctx, "target")
            .filter { it.setGameMode(gameType) }
        if (ctx.source.level.gameRules.getBoolean(GameRules.RULE_SENDCOMMANDFEEDBACK))
            players.forEach {
                it.sendMessage(gamemodeChangedText(gameType), Util.NIL_UUID)
            }

        ctx.success(textComponent("Changed the gamemode of ") {
            withStyle(ChatFormatting.GRAY)
            append(textComponent("${players.size} ${"player".apply { if (players.size != 1) plus("s") }}") {
                withStyle(ChatFormatting.RED, ChatFormatting.BOLD)
            })
            append(textComponent(".") { withStyle(ChatFormatting.GRAY) })
        }, true)
    }
}