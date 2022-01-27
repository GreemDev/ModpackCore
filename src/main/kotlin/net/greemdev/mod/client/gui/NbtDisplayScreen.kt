package net.greemdev.mod.client.gui

import com.mojang.blaze3d.vertex.PoseStack
import net.greemdev.mod.util.*
import net.minecraft.ChatFormatting
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.components.Button
import net.minecraft.client.gui.components.ObjectSelectionList
import net.minecraft.client.gui.screens.Screen
import net.minecraft.nbt.CompoundTag
import net.minecraft.network.chat.Component
import net.minecraft.network.chat.TextComponent

// This is the Kotlin implementation of SilentLib's system.
// It was used as a reference. Heavy reference. If you can't tell.

class NbtDisplayScreen(val nbt: CompoundTag, titleIn: Component) : Screen(titleIn) {

    var header: Component = title
    val mc by invoking { minecraft ?: Minecraft.getInstance() }

    val lines by invoking {
        val base = NBTUtil.formatNbt(nbt, 0)
        return@invoking arrayListOf<String>().apply {
            add(textBuilder().withReset("{").format())
            base.forEach { add(NBTUtil.indent(1) + it) }
            add(textBuilder().withReset("}").format())
        }
    }
    lateinit var displayList: NbtDisplayList

    override fun init() {
        val scaledWidth = mc.window.guiScaledWidth
        val scaledHeight = mc.window.guiScaledHeight
        val width = 100
        val height = 20
        addRenderableWidget(Button(scaledWidth - (width + 5) - 2, scaledHeight - (height + 20) - 2,
            width, height, textComponent("Export to JSON") { withStyle(ChatFormatting.GREEN) }) {
            val jObj = NBTUtil.Json.toJObject(nbt)
            val message = NBTUtil.Json.save(jObj)
            header = textComponent(message)
        })
        displayList = NbtDisplayList(this, mc, scaledWidth + 80, this.height, 12, this.height - 12, 11).also(::addWidget)
    }

    override fun render(pPoseStack: PoseStack, pMouseX: Int, pMouseY: Int, pPartialTick: Float) {
        displayList.render(pPoseStack, pMouseX, pMouseY, pPartialTick)
        val scaledWidth = mc.window.guiScaledWidth
        renderText {
            scaled(pPoseStack, font, textComponent(header.string).visualOrderText, (scaledWidth - font.width(header.string)) / 2, 2, 1f, 0xFFFFFF, true)
        }
        super.render(pPoseStack, pMouseX, pMouseY, pPartialTick)
    }

}

class NbtDisplayList(screen: NbtDisplayScreen, mc: Minecraft, width: Int, height: Int, top: Int, bottom: Int, slotHeight: Int)
    : ObjectSelectionList<NbtDisplayList.Entry>(mc, width, height, top, bottom, slotHeight) {

    init {
        screen.lines.forEach { addEntry(NbtDisplayList.Entry(it)) }
    }

    override fun getRowWidth(): Int = super.getRowWidth() + 200

    class Entry(val text: String) : ObjectSelectionList.Entry<Entry>() {
        override fun render(
            matrix: PoseStack,
            pIndex: Int,
            pTop: Int,
            pLeft: Int,
            pWidth: Int,
            pHeight: Int,
            pMouseX: Int,
            pMouseY: Int,
            pIsMouseOver: Boolean,
            pPartialTick: Float
        ) = renderText {
            scaled(matrix, Minecraft.getInstance().font, textComponent(text).visualOrderText, pLeft, pTop, 1.15f, 0xFFFFFF, true)
        }

        override fun getNarration(): Component = TextComponent.EMPTY
    }
}