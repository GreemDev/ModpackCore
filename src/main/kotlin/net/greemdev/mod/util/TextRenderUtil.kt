package net.greemdev.mod.util

import com.mojang.blaze3d.vertex.PoseStack
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.Font
import net.minecraft.network.chat.FormattedText
import net.minecraft.util.FormattedCharSequence

object TextRenderingContext {
    fun scaled(matrix: PoseStack, fontRenderer: Font, text: FormattedCharSequence, x: Int, y: Int, scale: Float, color: Int, shadow: Boolean)
        = TextRenderUtil.renderScaled(matrix, fontRenderer, text, x, y, scale, color, shadow)

    fun split(matrix: PoseStack, fontRenderer: Font, text: FormattedText, x: Int, y: Int, width: Int, color: Int, shadow: Boolean)
        = TextRenderUtil.renderSplit(matrix, fontRenderer, text, x, y, width, color, shadow)

    fun splitScaled(matrix: PoseStack, fontRenderer: Font, text: FormattedText, x: Int, y: Int, scale: Float, color: Int, shadow: Boolean, length: Int)
        = TextRenderUtil.renderSplitScaled(matrix, fontRenderer, text, x, y, scale, color, shadow, length)
}

fun renderText(renderer: TextRenderingContext.() -> Unit) {
    TextRenderingContext.renderer()
}

object TextRenderUtil {

    val fontRenderer by invoking { Minecraft.getInstance().font }

    private fun render(matrix: PoseStack, fontRenderer: Font, text: FormattedCharSequence, x: Float, y: Float, color: Int, shadow: Boolean) {
        if (shadow)
            fontRenderer.drawShadow(matrix, text, x, y, color)
        else
            fontRenderer.draw(matrix, text, x, y, color)
    }

    fun renderScaled(matrix: PoseStack, fontRenderer: Font, text: FormattedCharSequence, x: Int, y: Int, scale: Float, color: Int, shadow: Boolean) {
        matrix.pushPose()
        matrix.scale(scale, scale, scale)
        render(matrix, fontRenderer, text, x / scale, y / scale, color, shadow)
        matrix.popPose()
    }

    fun renderSplit(matrix: PoseStack, fontRenderer: Font, text: FormattedText, x: Int, y: Int, width: Int, color: Int, shadow: Boolean) {
        fontRenderer.split(text, width).forEachIndexed { i, line ->
            val translatedY = y + (i * fontRenderer.lineHeight)
            render(matrix, fontRenderer, line, x.toFloat(), translatedY.toFloat(), color, shadow)
        }
    }

    fun renderSplitScaled(matrix: PoseStack, fontRenderer: Font, text: FormattedText, x: Int, y: Int, scale: Float, color: Int, shadow: Boolean, length: Int) {
        fontRenderer.split(text, (length / scale).toInt()).forEachIndexed { i, line ->
            val translatedY = y + (i * (fontRenderer.lineHeight * scale + 3))
            renderScaled(matrix, fontRenderer, line, x, translatedY.toInt(), scale, color, shadow)
        }
    }

}