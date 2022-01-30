@file:JvmName("Args")
package net.greemdev.mod.command.api

import com.mojang.brigadier.arguments.*
import net.minecraft.advancements.critereon.MinMaxBounds
import net.minecraft.commands.arguments.*
import net.minecraft.commands.arguments.blocks.*
import net.minecraft.commands.arguments.coordinates.*
import net.minecraft.commands.arguments.item.*

val arg = Arguments

inline infix fun <reified T> MinecraftCommandContext.argument(name: String) = getArgument(name, T::class.java)

inline fun<reified T> MinecraftCommandContext.argument(name: String, noinline parser: (MinecraftCommandContext, String) -> T)
    = parser(this, name)

object Arguments {
    fun boolean(): BoolArgumentType = BoolArgumentType.bool()
    fun double(min: Double = -Double.MAX_VALUE, max: Double = Double.MAX_VALUE): DoubleArgumentType = DoubleArgumentType.doubleArg(min, max)
    fun float(min: Float = -Float.MAX_VALUE, max: Float = Float.MAX_VALUE): FloatArgumentType = FloatArgumentType.floatArg(min, max)
    fun int(min: Int = Int.MIN_VALUE, max: Int = Int.MAX_VALUE): IntegerArgumentType = IntegerArgumentType.integer(min, max)
    fun long(min: Long = Long.MIN_VALUE, max: Long = Long.MAX_VALUE): LongArgumentType = LongArgumentType.longArg(min, max)
    fun wordString(): StringArgumentType = StringArgumentType.word()
    fun quotableString(): StringArgumentType = StringArgumentType.string()
    fun greedyString(): StringArgumentType = StringArgumentType.greedyString()
    fun blockPredicate(): BlockPredicateArgument = BlockPredicateArgument.blockPredicate()
    fun blockState(): BlockStateArgument = BlockStateArgument.block()
    fun blockPos(): BlockPosArgument = BlockPosArgument.blockPos()
    fun columnPos(): ColumnPosArgument = ColumnPosArgument.columnPos()
    fun rotation(): RotationArgument = RotationArgument.rotation()
    fun swizzle(): SwizzleArgument = SwizzleArgument.swizzle()
    fun vec2(centerCorrect: Boolean = true): Vec2Argument = Vec2Argument.vec2(centerCorrect)
    fun vec3(centerCorrect: Boolean = true): Vec3Argument = Vec3Argument.vec3(centerCorrect)
    fun function(): FunctionArgument = FunctionArgument.functions()
    fun item(): ItemArgument = ItemArgument.item()
    fun itemPredicate(): ItemPredicateArgument = ItemPredicateArgument.itemPredicate()
    fun angle(): AngleArgument = AngleArgument.angle()
    fun color(): ColorArgument = ColorArgument.color()
    fun textComponent(): ComponentArgument = ComponentArgument.textComponent()
    fun compoundTag(): CompoundTagArgument = CompoundTagArgument.compoundTag()
    fun dimension(): DimensionArgument = DimensionArgument.dimension()
    fun entityAnchor(): EntityAnchorArgument = EntityAnchorArgument.anchor()
    fun entity(): EntityArgument = EntityArgument.entity()
    fun entities(): EntityArgument = EntityArgument.entities()
    fun playerEntity(): EntityArgument = EntityArgument.player()
    fun playerEntities(): EntityArgument = EntityArgument.players()
    fun entityId(): EntitySummonArgument = EntitySummonArgument.id()
    fun gameProfile(): GameProfileArgument = GameProfileArgument.gameProfile()
    fun enchantment(): ItemEnchantmentArgument = ItemEnchantmentArgument.enchantment()
    fun message(): MessageArgument = MessageArgument.message()
    fun effect(): MobEffectArgument = MobEffectArgument.effect()
    fun nbtPath(): NbtPathArgument = NbtPathArgument.nbtPath()
    fun nbtTag(): NbtTagArgument = NbtTagArgument.nbtTag()
    fun objective(): ObjectiveArgument = ObjectiveArgument.objective()
    fun objectiveCriteria(): ObjectiveCriteriaArgument = ObjectiveCriteriaArgument.criteria()
    fun operation(): OperationArgument = OperationArgument.operation()
    fun particle(): ParticleArgument = ParticleArgument.particle()
    fun intRange(): RangeArgument<MinMaxBounds.Ints> = RangeArgument.intRange()
    fun floatRange(): RangeArgument<MinMaxBounds.Doubles> = RangeArgument.floatRange()
    fun resourceLocation(): ResourceLocationArgument = ResourceLocationArgument.id()
    fun scoreboardSlot(): ScoreboardSlotArgument = ScoreboardSlotArgument.displaySlot()
    fun scoreHolder(): ScoreHolderArgument = ScoreHolderArgument.scoreHolder()
    fun scoreHolders(): ScoreHolderArgument = ScoreHolderArgument.scoreHolders()
    fun slot(): SlotArgument = SlotArgument.slot()
    fun team(): TeamArgument = TeamArgument.team()
    fun time(): TimeArgument = TimeArgument.time()
    fun uuid(): UuidArgument = UuidArgument.uuid()
}