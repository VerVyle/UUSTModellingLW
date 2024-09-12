package com.vervyle.lab1.util

import kotlin.math.log
import kotlin.random.Random

@Suppress("Warnings")
class Generator {
    private var random = Random(SEED)

    var lambda: Float = DEFAULT_LAMBDA

    fun generatePoissonValue(
    ): Int {
        var poissonVal = 0
        var time = 0.0
        val limit = 1.0
        var expVal: Double

        while (true) {
            expVal = generateExponentialValue()
            time += expVal
            if (time > limit)
                return poissonVal
            poissonVal += 1
        }
    }

    fun generateExponentialValue() = -1.0 * log(1 - random.nextDouble(), Math.E) / lambda

    fun setSeed(seed: Int) {
        random = Random(seed)
    }

    companion object {
        const val SEED = 0
        const val DEFAULT_LAMBDA = 1f
    }
}