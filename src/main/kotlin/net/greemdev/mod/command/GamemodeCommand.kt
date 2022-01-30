package net.greemdev.mod.command

import net.greemdev.mod.command.api.*
import net.greemdev.mod.util.*
import net.minecraft.ChatFormatting
import net.minecraft.Util
import net.minecraft.commands.arguments.EntityArgument
import net.minecraft.world.level.GameRules
import net.minecraft.world.level.GameType
import net.minecraft.world.level.GameType.*

class GamemodeCommand : ModCommand("gamemode") {
    init {
        literal {
            then("c") {
                executes(self(CREATIVE))
                then("target", arg.playerEntities()) {
                    executes(others(CREATIVE))
                }
            }
            then("s") {
                executes(self(DEFAULT_MODE))
                then("target", arg.playerEntities()) {
                    executes(others(DEFAULT_MODE))
                }
            }
            then("a") {
                executes(self(ADVENTURE))
                then("target", arg.playerEntities()) {
                    executes(others(ADVENTURE))
                }
            }
            then("sp") {
                executes(self(SPECTATOR))
                then("target", arg.playerEntities()) {
                    executes(others(SPECTATOR))
                }
            }
            GameType.values().forEach {
                then(it.getName()) {
                    executes(self(it))
                    then("target", arg.playerEntities()) {
                        executes(others(it))
                    }
                }
            }
        }
    }

    private fun colorFromGameType(gameType: GameType): ChatFormatting = when (gameType) {
        SURVIVAL -> ChatFormatting.DARK_RED
        ADVENTURE -> ChatFormatting.GOLD
        CREATIVE -> ChatFormatting.AQUA
        else -> ChatFormatting.DARK_AQUA
    }

    private fun gamemodeChangedText(gameType: GameType) =
        textComponent("Your gamemode has been changed to ") {
            withStyle(ChatFormatting.GRAY)
            append(textComponent(gameType.getName().replaceFirstChar(Char::uppercase)) {
                withStyle(colorFromGameType(gameType))
            })
            append(textComponent(".") { withStyle(ChatFormatting.GRAY) })
        }

    fun self(gameType: GameType) = run { ctx ->
        if (ctx.source.playerOrException.setGameMode(gameType)) {
            ctx.success(gamemodeChangedText(gameType), true)
        } else false
    }

    fun others(gameType: GameType) = run { ctx ->
        val players = ctx.argument("target", EntityArgument::getPlayers)
            .filter { it.setGameMode(gameType) }
        if (ctx.source.level.gameRules.getBoolean(GameRules.RULE_SENDCOMMANDFEEDBACK))
            players.forEach {
                it.sendMessage(gamemodeChangedText(gameType), Util.NIL_UUID)
            }

        ctx.success(textComponent("Changed the gamemode of ") {
            withStyle(ChatFormatting.GRAY)
            append(textComponent("${players.size} ${"player".quantify(players.size)}") {
                withStyle(ChatFormatting.RED, ChatFormatting.BOLD)
            })
            append(textComponent(".") { withStyle(ChatFormatting.GRAY) })
        }, true)
    }
}