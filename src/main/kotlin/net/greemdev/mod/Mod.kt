package net.greemdev.mod

import com.google.gson.JsonElement
import com.google.gson.JsonObject
import net.greemdev.mod.config.ModCfg
import net.greemdev.mod.init.Initializable
import net.greemdev.mod.init.ModRegistry
import net.greemdev.mod.network.packet.NbtDisplayPacket
import net.greemdev.mod.util.*
import net.minecraft.resources.ResourceLocation
import net.minecraftforge.event.AddReloadListenerEvent
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.config.ModConfig
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent
import net.minecraftforge.network.NetworkRegistry
import net.minecraftforge.network.simple.SimpleChannel
import net.minecraftforge.registries.DeferredRegister
import net.minecraftforge.registries.ForgeRegistryEntry
import net.minecraftforge.registries.IForgeRegistry
import net.minecraftforge.registries.IForgeRegistryEntry
import thedarkcolour.kotlinforforge.forge.MOD_BUS
import java.lang.IllegalArgumentException
import java.net.URI
import java.net.URL
import java.nio.file.Path
import java.util.*

@Mod(GreemMod.id)
object GreemMod {
    const val id = "greempackcore"

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

    @JvmStatic
    fun config() = ModCfg
    fun networking() = Network.channel

    fun <T : IForgeRegistryEntry<T>> newRegister(registry: IForgeRegistry<T>): DeferredRegister<T> 
        = DeferredRegister.create(registry, id)

    val logger by log4j(id)

    init {
        ModRegistry.registerAll()
        Network.init()
        ModCfg.init()
    }
}