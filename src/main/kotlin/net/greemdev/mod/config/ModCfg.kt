package net.greemdev.mod.config

import com.google.gson.GsonBuilder
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import net.greemdev.mod.util.*
import java.io.File
import java.lang.IllegalArgumentException
import java.util.*

object ModCfg {

    private lateinit var config: JsonObject

    fun reload() {
        config = read()
    }

    private fun nested(jobj: JsonObject, path: String): JsonElement {
        val parts = path.split('.')
        if (parts.isEmpty())
            throw IllegalArgumentException()
        if (parts.size == 1)
            return jobj.get(parts[0])
        if (parts.size == 2)
            return jobj.getAsJsonObject(parts[0]).get(parts[1])

        var current = jobj.getAsJsonObject(parts[0])
        parts.drop(1).dropLast(1).forEach {
            current = current.getAsJsonObject(it)
        }
        return current.get(parts.last())
    }
    fun getObject(key: String): JsonObject {
        return nested(config, key).asJsonObject
    }
    fun get(key: String): JsonElement {
        return nested(config, key)
    }

    private fun default(): JsonObject = jsonObject {
        add("qol") {
            addProperty("coloredEnchantments", true)
            addProperty("removePotionEnchantGlint", true)
        }
        add("enchanting") {
            add("mending") {
                addProperty("obtainableInEnchantmentTable", true)
                addProperty("chance", 25)
            }
        }
        add("minecraft") {
            addProperty("windowTitle", "mod")
        }
    }
    val gson = GsonBuilder().setPrettyPrinting().create()
    fun file() = File("config/greempack.json").apply {
        if (exists()) {
            setReadable(true)
            setWritable(true)
        }
    }
    fun init() {
        file().takeIf { it.exists() } ?: write()
        config = read()
    }
    fun write(config: JsonObject = default()) = file().writeText(gson.toJson(config))
    fun read(): JsonObject
        = try {
            gson.fromJson(file().readText(), JsonObject::class.java)
        } catch (t: Throwable) {
            t.printStackTrace()
            default()
        }
}