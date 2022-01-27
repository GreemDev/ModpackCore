@file:Suppress("unused", "MemberVisibilityCanBePrivate") //API

package net.greemdev.mod.util

import net.minecraft.ChatFormatting
import net.minecraft.ChatFormatting.*

fun textBuilder(initial: String = ""): FormattedTextBuilder = FormattedTextBuilder(StringBuilder(initial))
data class FormattedTextBuilder(private val raw: StringBuilder = StringBuilder()) {

    fun format(): String = raw.toString()

    fun withFormatting(vararg formatting: ChatFormatting): FormattedTextBuilder {
        formatting.forEach { this.raw.append(it.toString()) }
        return this
    }

    fun withReset(text: String, resetAtEnd: Boolean = false) = append(text, arrayOf(RESET), resetAtEnd = resetAtEnd)
    fun withBlack(text: String, resetAtEnd: Boolean = false) = append(text, arrayOf(BLACK), resetAtEnd = resetAtEnd)
    fun withDarkBlue(text: String, resetAtEnd: Boolean = false) = append(text, arrayOf(DARK_BLUE), resetAtEnd = resetAtEnd)
    fun withDarkGreen(text: String, resetAtEnd: Boolean = false) = append(text, arrayOf(DARK_GREEN), resetAtEnd = resetAtEnd)
    fun withDarkAqua(text: String, resetAtEnd: Boolean = false) = append(text, arrayOf(DARK_AQUA), resetAtEnd = resetAtEnd)
    fun withDarkRed(text: String, resetAtEnd: Boolean = false) = append(text, arrayOf(DARK_RED), resetAtEnd = resetAtEnd)
    fun withDarkPurple(text: String, resetAtEnd: Boolean = false) = append(text, arrayOf(DARK_PURPLE), resetAtEnd = resetAtEnd)
    fun withDarkGrey(text: String, resetAtEnd: Boolean = false) = append(text, arrayOf(DARK_GRAY), resetAtEnd = resetAtEnd)
    fun withGold(text: String, resetAtEnd: Boolean = false) = append(text, arrayOf(GOLD), resetAtEnd = resetAtEnd)
    fun withGrey(text: String, resetAtEnd: Boolean = false) = append(text, arrayOf(GRAY), resetAtEnd = resetAtEnd)
    fun withBlue(text: String, resetAtEnd: Boolean = false) = append(text, arrayOf(BLUE), resetAtEnd = resetAtEnd)
    fun withGreen(text: String, resetAtEnd: Boolean = false) = append(text, arrayOf(GREEN), resetAtEnd = resetAtEnd)
    fun withAqua(text: String, resetAtEnd: Boolean = false) = append(text, arrayOf(AQUA), resetAtEnd = resetAtEnd)
    fun withRed(text: String, resetAtEnd: Boolean = false) = append(text, arrayOf(RED), resetAtEnd = resetAtEnd)
    fun withLightPurple(text: String, resetAtEnd: Boolean = false) = append(text, arrayOf(LIGHT_PURPLE), resetAtEnd = resetAtEnd)
    fun withYellow(text: String, resetAtEnd: Boolean = false) = append(text, arrayOf(YELLOW), resetAtEnd = resetAtEnd)
    fun withWhite(text: String, resetAtEnd: Boolean = false) = append(text, arrayOf(WHITE), resetAtEnd = resetAtEnd)

    fun append(content: String?, prefixing: Array<ChatFormatting>?, suffixing: Array<ChatFormatting>? = null, resetAtEnd: Boolean = false): FormattedTextBuilder {
        optionalOf(prefixing)
            .ifPresent { withFormatting(*it) }
        optionalOf(content)
            .ifPresent { appendText(it) }
        optionalOf(suffixing)
            .ifPresent { withFormatting(*it) }
        if (resetAtEnd)
            withReset()
        return this
    }


    fun withObfuscated() = withFormatting(OBFUSCATED)
    fun withBold() = withFormatting(BOLD)
    fun withStrikethrough() = withFormatting(STRIKETHROUGH)
    fun withUnderline() = withFormatting(UNDERLINE)
    fun withItalic() = withFormatting(ITALIC)

    fun withReset() = withFormatting(RESET)

    fun appendText(text: String): FormattedTextBuilder {
        this.raw.append(text)
        return this
    }

    fun appendTextThenReset(text: String): FormattedTextBuilder {
        this.appendText(text).withReset()
        return this
    }
}