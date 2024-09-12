package com.vervyle.lab1

import androidx.lifecycle.ViewModel
import com.vervyle.lab1.util.Generator

class MainViewModel constructor(

) : ViewModel() {

    val generator = Generator()

    fun getExpVal(): Double {
        return generator.generateExponentialValue()
    }

    fun getPoisson(): Int = generator.generatePoissonValue()
}