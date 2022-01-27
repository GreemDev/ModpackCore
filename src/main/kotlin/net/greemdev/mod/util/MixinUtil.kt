package net.greemdev.mod.util

import java.util.*


object MixinUtil {
    val romanTreeMap = TreeMap<Int, String>().apply {
        put(1000, "M")
        put(900, "CM")
        put(500, "D")
        put(400, "CD")
        put(100, "C")
        put(90, "XC")
        put(50, "L")
        put(40, "XL")
        put(10, "X")
        put(9, "IX")
        put(5, "V")
        put(4, "IV")
        put(1, "I")
    }

    @JvmStatic
    fun romanFromArabic(number: Int): String? {
        if (number >= 4000) return "$number"
        if (number == 0) return "N"
        val l = romanTreeMap.floorKey(number)
        return if (number == l) {
            romanTreeMap[number]
        } else romanTreeMap[l] + romanFromArabic(number - l)
    }
}

