package net.greemdev.mod.init

import net.greemdev.mod.GreemMod
import net.greemdev.mod.command.CoreCommand
import net.greemdev.mod.command.NbtCommand
import net.greemdev.mod.item.ToolReforgerItem
import net.greemdev.mod.util.command
import net.minecraft.commands.arguments.EntityArgument
import net.minecraft.commands.arguments.coordinates.BlockPosArgument
import net.minecraft.core.particles.ParticleType
import net.minecraft.world.item.Item
import net.minecraft.world.level.GameType
import net.minecraftforge.event.RegisterCommandsEvent
import net.minecraftforge.eventbus.api.IEventBus
import net.minecraftforge.registries.DeferredRegister
import net.minecraftforge.registries.ForgeRegistries
import net.minecraftforge.registries.IForgeRegistry
import net.minecraftforge.registries.IForgeRegistryEntry
import thedarkcolour.kotlinforforge.forge.FORGE_BUS
import thedarkcolour.kotlinforforge.forge.MOD_BUS
import thedarkcolour.kotlinforforge.forge.registerObject
import kotlin.properties.ReadOnlyProperty

fun register(vararg holders: RegistryHolder<*>) = holders.forEach { it.init(MOD_BUS) }

object ModRegistry {
    fun registerAll() {
        //register(Items)
        FORGE_BUS.addListener(Commands::registerAll)
    }

    object Particles : RegistryHolder<ParticleType<*>>(ForgeRegistries.PARTICLE_TYPES) {

    }

    object Items : RegistryHolder<Item>(ForgeRegistries.ITEMS) {
        val toolReforger by registeringObject("tool_reforger") {
            ToolReforgerItem(ToolReforgerItem.properties())
        }
    }

    object Commands {
        fun registerAll(event: RegisterCommandsEvent) {
            CoreCommand().register(event.dispatcher)
        }
    }
}

abstract class RegistryHolder<T : IForgeRegistryEntry<T>>(val registry: DeferredRegister<T>) : TypedInitializable<IEventBus> {
    constructor(forgeRegistry: IForgeRegistry<T>) : this(GreemMod.newRegister(forgeRegistry))

    override fun init(parameter: IEventBus) = registry.register(parameter)

    fun registeringObject(
        name: String,
        supplier: () -> T
    ): ReadOnlyProperty<Any?, T> = registry.registerObject(name, supplier)
}