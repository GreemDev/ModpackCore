package net.greemdev.mod

import net.greemdev.mod.init.Initializable
import net.greemdev.mod.init.ModRegistry
import net.greemdev.mod.network.packet.NbtDisplayPacket
import net.greemdev.mod.util.log4j
import net.minecraft.resources.ResourceLocation
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent
import net.minecraftforge.network.NetworkRegistry
import net.minecraftforge.network.simple.SimpleChannel
import net.minecraftforge.registries.DeferredRegister
import net.minecraftforge.registries.ForgeRegistryEntry
import net.minecraftforge.registries.IForgeRegistry
import net.minecraftforge.registries.IForgeRegistryEntry
import org.spongepowered.asm.mixin.connect.IMixinConnector
import org.spongepowered.asm.mixin.extensibility.IMixinConfig

@Mod(GreemMod.id)
object GreemMod {

    object Network : Initializable {
        private val name = ResourceLocation(GreemMod.id, "network")
        private val ver = 1
        val channel: SimpleChannel = NetworkRegistry.ChannelBuilder.named(name)
            .clientAcceptedVersions { it == ver.toString() }
            .serverAcceptedVersions { it == ver.toString() }
            .networkProtocolVersion { ver.toString() }
            .simpleChannel()

        override fun init() {
            channel.messageBuilder(NbtDisplayPacket::class.java, 1)
                .decoder(NbtDisplayPacket.Companion::fromBytes)
                .encoder(NbtDisplayPacket::toBytes)
                .consumer(NbtDisplayPacket.Companion::handle)
                .add()
        }
    }

    fun networking() = Network.channel

    fun <T : IForgeRegistryEntry<T>> newRegister(registry: IForgeRegistry<T>): DeferredRegister<T>
        = DeferredRegister.create(registry, id)

    const val id = "greempackcore"

    val logger by log4j(id)

    init {
        ModRegistry.registerAll()
        Network.init()
    }
}