package net.greemdev.mod.network.packet

import net.greemdev.mod.client.gui.NbtDisplayScreen
import net.minecraft.client.Minecraft
import net.minecraft.nbt.CompoundTag
import net.minecraft.network.FriendlyByteBuf
import net.minecraft.network.chat.Component
import net.minecraftforge.network.NetworkEvent
import java.util.function.Supplier

data class NbtDisplayPacket(val nbt: CompoundTag, val title: Component) {
    companion object {
        fun fromBytes(buffer: FriendlyByteBuf): NbtDisplayPacket = NbtDisplayPacket(buffer.readNbt()!!, buffer.readComponent())
        fun handle(packet: NbtDisplayPacket, context: Supplier<NetworkEvent.Context>) {
            clientHandle(packet)
            context.get().packetHandled = true
        }
        private fun clientHandle(packet: NbtDisplayPacket) {
            val player = Minecraft.getInstance().player
            if (player != null) {
                Minecraft.getInstance().setScreen(NbtDisplayScreen(packet.nbt, packet.title))
            }
        }
    }
    fun toBytes(buffer: FriendlyByteBuf) {
        buffer.writeNbt(nbt)
        buffer.writeComponent(title)
    }
}