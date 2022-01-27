package net.greemdev.mod.item

import net.greemdev.mod.init.Initializable
import net.greemdev.mod.init.ItemPropertiesHolder
import net.greemdev.mod.util.itemProperties
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.inventory.CraftingContainer
import net.minecraft.world.item.CreativeModeTab
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.CraftingRecipe
import net.minecraft.world.item.crafting.RecipeSerializer
import net.minecraft.world.level.Level
import net.minecraftforge.common.crafting.IShapedRecipe
import net.minecraftforge.event.AnvilUpdateEvent
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent
import thedarkcolour.kotlinforforge.forge.FORGE_BUS

class ToolReforgerItem(props: Item.Properties) : GreemModItem(props) {
    companion object : Initializable, ItemPropertiesHolder {
        override fun properties() = itemProperties {
            stacksTo(1)
            tab(CreativeModeTab.TAB_TOOLS)
        }

        override fun init() {
            FORGE_BUS.addListener(::onAnvilUpdate)
        }

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
}

class ToolReforgerItemRecipe : CraftingRecipe, IShapedRecipe<CraftingContainer> {
    override fun matches(inventory: CraftingContainer, world: Level): Boolean {
        TODO("Not yet implemented")
    }

    override fun assemble(p_44001_: CraftingContainer): ItemStack {
        TODO("Not yet implemented")
    }

    override fun canCraftInDimensions(p_43999_: Int, p_44000_: Int): Boolean {
        TODO("Not yet implemented")
    }

    override fun getResultItem(): ItemStack {
        TODO("Not yet implemented")
    }

    override fun getId(): ResourceLocation {
        TODO("Not yet implemented")
    }

    override fun getSerializer(): RecipeSerializer<*> {
        TODO("Not yet implemented")
    }

    override fun getRecipeWidth() = 3

    override fun getRecipeHeight() = 3

}