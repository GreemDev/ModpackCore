package net.greemdev.mod.util

import com.google.gson.*
import net.greemdev.mod.GreemMod
import net.minecraft.ChatFormatting
import net.minecraft.nbt.*
import java.io.File


object NBTUtil {

    object Json {
        private val gson by invoking { GsonBuilder().setPrettyPrinting().create() }
        fun toJElement(nbt: Tag): JsonElement = when (nbt) {
            is CompoundTag -> toJObject(nbt)
            is CollectionTag<*> -> toJArray(nbt)
            is NumericTag -> com.google.gson.JsonPrimitive(nbt.asNumber)
            is StringTag -> com.google.gson.JsonPrimitive(nbt.asString)
            else -> JsonNull.INSTANCE
        }

        fun toJObject(nbt: CompoundTag) = JsonObject().apply {
            nbt.allKeys.forEach { key ->
                optionalOf(nbt[key]).ifPresent { add(key, toJElement(it)) }
            }
        }

        fun toJArray(nbt: CollectionTag<*>) = JsonArray().apply {
            nbt.forEach { add(toJElement(it)) }
        }

        fun save(json: JsonObject): String {
            val fileName = "nbtExport-${randomShortUuid()}.json"
            val outputFile = File("exportedNBT", fileName)
            val dir = outputFile.parentFile

            if (!dir.exists() && !dir.mkdirs()) {
                GreemMod.logger.error("Could not create directory ${outputFile.parent}")
                return "Could not create NBT export directory."
            }

            return try {
                outputFile.writeText(gson.toJson(json))
                "Wrote NBT data to ${outputFile.path}".apply(GreemMod.logger::info)
            } catch (t: Throwable) {
                t.printStackTrace()
                t.message ?: t.toString()
            }
        }
    }

    fun formatNbt(nbt: CompoundTag?, depth: Int): List<String> {
        val list: MutableList<String> = ArrayList()
        for (key in nbt!!.allKeys) {
            list.addAll(formatNbt(key, nbt[key], depth + 1))
        }
        return list
    }

    fun formatNbt(key: String, nbt: Tag?, depth: Int): List<String> {
        val list = arrayListOf<String>()
        val indentStr = indent(depth)
        when (nbt) {
            is CompoundTag -> formatCompound(key, nbt as CompoundTag?, depth, list, indentStr)
            is CollectionTag<*> -> formatList(key, nbt as CollectionTag<*>?, depth, list, indentStr)
            is NumericTag -> formatNumber(key, nbt as NumericTag?, list, indentStr)
            is StringTag -> list.add(indentStr + format(key, nbt.asString, ChatFormatting.GREEN))
        }
        return list
    }

    fun formatCompound(
        key: String,
        nbt: CompoundTag?,
        depth: Int,
        list: MutableList<String>,
        indentStr: String
    ) {
        if (nbt!!.isEmpty) {
            list.add(indentStr + format(key, "{}", ChatFormatting.DARK_GREEN))
        } else {
            list.add("$indentStr${format(key, "{", ChatFormatting.DARK_GREEN)}")
            list.addAll(formatNbt(nbt, depth + 1))
            list.add(textBuilder(indentStr).withDarkGreen("}", true).appendText(darkGrayOrEmpty(key, "#")).format())
        }
    }

    private fun darkGrayOrEmpty(string: String, prefix: String)
        = if (string.isEmpty()) "" else "${ChatFormatting.DARK_GRAY} $prefix$string"

    fun formatList(
        key: String,
        nbt: CollectionTag<*>?,
        depth: Int,
        list: MutableList<String>,
        indentStr: String
    ) {
        if (nbt!!.isEmpty()) {
            list.add(indentStr + format(key, "[]", ChatFormatting.YELLOW))
        } else {
            list.add(indentStr + format(key, "[", ChatFormatting.YELLOW))
            for (element in (nbt as CollectionTag<*>?)!!) {
                list.addAll(formatNbt("", element, depth + 1))
            }
            list.add(textBuilder(indentStr).withYellow("]", true).appendText(darkGrayOrEmpty(key, "#")).format())
        }
    }

    fun formatNumber(key: String, nbt: NumericTag?, list: MutableList<String>, indentStr: String) {
        val value = nbt!!.asNumber
        var line = indentStr + format(key, value, ChatFormatting.LIGHT_PURPLE)
        if (value is Int) {
            line += ChatFormatting.GRAY.toString() + String.format(" (0x%X)", value.toInt())
        }
        list.add(line)
    }

    fun format(key: String, value: Any, valueFormat: ChatFormatting) = if (key.isEmpty()) {
        "$valueFormat$value"
    } else {
        textBuilder().withGold(key, true).appendText(": $valueFormat$value").format()
    }


    fun indent(depth: Int): String = buildString {
        repeat(depth) {
            append("  ")
        }
    }
}