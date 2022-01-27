package net.greemdev.mod.item

import net.minecraft.world.item.Item
import net.minecraftforge.event.AnvilUpdateEvent
import net.minecraftforge.eventbus.api.SubscribeEvent

abstract class GreemModItem(val itemProperties: Item.Properties) : Item(itemProperties)

class ForgeHandler {

    @SubscribeEvent
    fun onAnvilUpdate(event: AnvilUpdateEvent) {
        if (event.output.isEmpty && (event.left.isEmpty || event.right.isEmpty)) return
        val out = event.output
        if (event.right.item is ToolReforgerItem)
            out.setRepairCost(0)
        if (event.cost == 0)
            event.cost = 1
        event.output = out
    }

}