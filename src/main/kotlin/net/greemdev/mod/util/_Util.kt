@file:JvmName("MiscUtil")
package net.greemdev.mod.util

import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import org.apache.logging.log4j.message.MessageFactory
import java.nio.ByteBuffer
import java.util.*
import java.util.stream.Stream
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KClass
import kotlin.reflect.KProperty

fun String.quantify(quantity: Number, useES: Boolean = false, prefixQuantity: Boolean = true): String {
    return if (quantity != 1)
        "${if (prefixQuantity) quantity else ""}${this}${if (useES) "es" else "s"}"
    else this
}

/**
 * Returns the item in the main hand, or the item in the offhand if there is no item in the main hand.
 */
val LivingEntity.heldItem: ItemStack
    get() = if (!mainHandItem.isEmpty) mainHandItem else offhandItem

fun randomUuid() = UUID.randomUUID()
fun randomShortUuid() = randomUuid().shorten()

fun String.parseUUID(): UUID? {
    return tryOrNull {
        UUID.fromString(this)
    } ?: tryOrNull {
        val newBuffer = ByteBuffer.wrap(Base64.getUrlDecoder().decode("$this=="))
        UUID(newBuffer.long, newBuffer.long)
    }
}

fun UUID.shorten(): String {
    val buffer = ByteBuffer.wrap(ByteArray(16)).apply {
        putLong(mostSignificantBits)
        putLong(leastSignificantBits)
    }

    return Base64.getUrlEncoder()
        .encodeToString(buffer.array())
        .trimEnd { it == '=' }
}

fun<T> tryOrNull(func: () -> T): T? = try { func() } catch (t: Throwable) { null }

/**
 * Looks repetitive however each different type we check for has its own special logic in [LogManager]
 */
fun log4j(value: Any): ReadOnlyProperty<Any, Logger> = invoking {
    when (value) {
        is String -> LogManager.getLogger(value)
        is Class<*> -> LogManager.getLogger(value)
        is KClass<*> -> LogManager.getLogger(value.java)
        is MessageFactory -> LogManager.getLogger(value)
        else -> LogManager.getLogger(value)
    }
}

fun<T> optionalOf(value: T? = null): Optional<T> = if (value == null) Optional.empty() else Optional.of(value)

fun <T> invoking(func: () -> T): FunctionProperty<T> = FunctionProperty(func)
fun <T> invokingOrNull(func: () -> T): FunctionProperty<T?> = FunctionProperty{tryOrNull(func)}

class FunctionProperty<T>(private val producer: () -> T): PropertyValue<T> {
    override fun get(thisRef: Any?): T = producer()
}

interface PropertyValue<T> : ReadOnlyProperty<Any?, T> {
    override fun getValue(thisRef: Any?, property: KProperty<*>): T = get(thisRef)
    fun get(thisRef: Any?): T
}


