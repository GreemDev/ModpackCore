package net.greemdev.mod.init

import net.minecraft.world.item.Item

interface Initializable {
    fun init()
}

interface TypedInitializable<T> {
    fun init(parameter: T)
}

interface ItemPropertiesHolder {
    fun properties(): Item.Properties
}

