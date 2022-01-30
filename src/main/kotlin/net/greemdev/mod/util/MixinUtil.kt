package net.greemdev.mod.util

import java.util.*
import java.util.concurrent.ThreadLocalRandom


fun String.chopRest(str: String): String = if (contains(str))
    substring(0, this.indexOf(str))
else this

object MixinUtil {

    @JvmStatic
    fun isInRange(value: Int, lower: Int, upper: Int, inclusiveUpper: Boolean): Boolean {
        return value >= lower && (value < upper || (value <= upper && inclusiveUpper))
    }

    @JvmStatic
    fun isInRange(value: Int, lower: Int, upper: Int): Boolean {
        return isInRange(value, lower, upper, false)
    }

    /**
     * Allows easy randomization: serves a `x in y` chance purpose, where x is [lowerBound] and y is [upperBound].
     * Example: rng(1, 100) ("1 in 100 chance") will only return true if the internal randomizer returns the lower bound,
     * this example will return true once every one hundred times statistically speaking.
     *
     * both bounds are inclusive
     */
    @JvmStatic
    fun rng(lowerBound: Number, upperBound: Number): Boolean {
        val lower = "$lowerBound".chopRest(".") //ignore decimal points and the digits after it
        val upper = "$upperBound".chopRest(".")
        return ThreadLocalRandom.current().nextLong(lower.toLong(), upper.toLong().inc()) == lowerBound
    }

    @JvmStatic
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

