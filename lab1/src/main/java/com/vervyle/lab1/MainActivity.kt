package com.vervyle.lab1

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.vervyle.lab1.ui.theme.UUSTModellingLWTheme
import org.apache.commons.math3.distribution.ExponentialDistribution
import org.apache.commons.math3.distribution.PoissonDistribution
import org.apache.commons.math3.util.BigReal
import java.lang.Math.pow
import java.math.BigDecimal
import kotlin.math.exp
import kotlin.math.floor
import kotlin.math.log
import kotlin.math.pow
import kotlin.math.sqrt

class MainActivity : ComponentActivity() {

    private val viewModel = MainViewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            UUSTModellingLWTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Main(
                        viewModel = viewModel, modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

const val LIST_SIZE = 100

const val TAG = "lab1"

@Composable
fun Main(
    viewModel: MainViewModel, modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        viewModel.generator.lambda = 10f
        val poissonList = List(LIST_SIZE) { viewModel.getPoisson() }
        viewModel.generator.lambda = 0.1f
        val exponentialList = List(LIST_SIZE) { viewModel.getExpVal() }
        item {
            Text(
                text = run {
                    "random exponential value generation\n"
                }, modifier = modifier
            )
            Text(
                text = run {
                    var mean = 0.0
                    exponentialList.forEach {
                        mean += it
                    }
                    mean /= exponentialList.size
                    "mean is ".plus(mean).plus(
                        "\n with lambda equal " + "${viewModel.generator.lambda} \n"
                    )
                }, modifier = modifier
            )
            Text(
                text = run {
                    val k = floor(1 + log(LIST_SIZE.toDouble(), 2.0)).toInt()
                    val bins = List(k) { index ->
                        index * exponentialList.max() / k
                    }
                    val mean = run {
                        var sum: Double = 0.0
                        exponentialList.forEach {
                            sum += it
                        }
                        sum / exponentialList.size
                    }
                    val observedFreq = MutableList(k) {
                        0
                    }
                    exponentialList.forEach { expVal ->
                        bins.indices.forEach { index ->
                            if (index == bins.size - 1) {
                                if (expVal >= bins[index]) {
                                    observedFreq[index]++
                                }
                            } else if (expVal >= bins[index] && expVal < bins[index + 1]) {
                                observedFreq[index]++
                            }
                        }
                    }
                    val expectedFreq = DoubleArray(observedFreq.size) { 0.0 }
                    for (i in expectedFreq.indices) {
                        val lowerBound = bins[i]
                        val upperBound =
                            if (i != expectedFreq.size - 1) bins[i + 1] else exponentialList.max()
                        expectedFreq[i] =
                            exponentialList.size * (ExponentialDistribution(mean).cumulativeProbability(
                                upperBound
                            ) - ExponentialDistribution(mean).cumulativeProbability(lowerBound))
                    }
                    var expectedFreq2 = DoubleArray(observedFreq.size) { 0.0 }
                    for (i in expectedFreq2.indices) {
                        if (i == expectedFreq2.size - 1) {
                            expectedFreq2[i] =
                                exponentialList.size * (1.0.minus(exp(-1.0 * exponentialList.max() / mean)))
                        } else {
                            expectedFreq2[i] =
                                exponentialList.size * (1.0.minus(exp(-1.0 * bins[i + 1] / mean)))
                        }
                    }
                    var pAlpha = 0.0
                    expectedFreq2 = expectedFreq2.mapIndexed { index, freq ->
                        if (index == 0)
                            freq
                        else
                            freq - expectedFreq2[index - 1]
                    }.toDoubleArray()
                    for (i in 0..expectedFreq2.size - 1) {
                        pAlpha += (pow(
                            expectedFreq2[i] - observedFreq[i].toDouble(), 2.0
                        )) / expectedFreq2[i].toDouble()
                        Log.d(TAG, "Main: ${(pow(
                            expectedFreq2[i] - observedFreq[i].toDouble(), 2.0
                        )) / expectedFreq2[i].toDouble()} ")
                    }
//                    expectedFreq2
//                        .zip(observedFreq) { expected, observed ->
//                            if (observed == 0)
//                                return@zip
//                            pAlpha += (pow(
//                                expected - observed.toDouble(),
//                                2.0
//                            )) / observed.toDouble()
//                        }
                    "bins:\n${bins.map { String.format("%.2f", it) }} \n".plus("observedFreq:\n${
                        observedFreq.map {
                            String.format(
                                "%.2f", it.toDouble()
                            )
                        }
                    } \n")
//                        .plus("expectedFreq (Library):\n${
//                        expectedFreq.map {
//                            String.format(
//                                "%.2f", it
//                            )
//                        }
//                    } \n")
                        .plus("expectedFreq:\n${
                            expectedFreq2.map {
                                String.format(
                                    "%.2f", it
                                )
                            }
                        } \n")
                        .plus(
                            "pAlpha: $pAlpha \n"
                        )
                        .plus("alpha for n = ${k - 2}: 11.1\n")
                        .plus("is valid? ${if (pAlpha < 15.5) "H0 is valid" else "H0 is invalid"}")
                }, modifier = modifier
            )
        }
        item {
            HorizontalDivider()
        }
        item {
            Text(
                text = run {
                    "poisson random value generation\n"
                }, modifier = modifier
            )
            Text(
                text = run {
                    var mean = 0.0
                    poissonList.forEach {
                        mean += it
                    }
                    mean /= poissonList.size
                    "mean is ".plus(mean).plus(
                        "\n with lambda equal " + "${viewModel.generator.lambda} \n"
                    )
                }, modifier = modifier
            )
            Text(
                text = run {
                    val k = floor(1 + log(LIST_SIZE.toDouble(), 2.0)).toInt()
                    val bins = List(k) { index ->
                        index * poissonList.max() / k
                    }
                    val mean = run {
                        var sum: Double = 0.0
                        poissonList.forEach {
                            sum += it
                        }
                        sum / poissonList.size
                    }
                    val lambda = mean
                    val observedFreq = MutableList(k) {
                        0
                    }
                    poissonList.forEach { poissonVal ->
                        bins.indices.forEach { index ->
                            if (index == bins.size - 1) {
                                if (poissonVal >= bins[index])
                                    observedFreq[index]++
                            } else if (poissonVal >= bins[index] && poissonVal < bins[index + 1]) {
                                observedFreq[index]++
                            }
                        }
                    }
                    val expectedFreq = DoubleArray(observedFreq.size) { 0.0 }
                    for (i in expectedFreq.indices) {
                        val lowerBound = bins[i]
                        val upperBound: Int
                        if (i != expectedFreq.size - 1)
                            upperBound = bins[i + 1]
                        else
                            upperBound = poissonList.max()
                        expectedFreq[i] =
                            exponentialList.size * (PoissonDistribution(mean).cumulativeProbability(
                                upperBound
                            ) - PoissonDistribution(mean).cumulativeProbability(lowerBound))
                    }
                    val expectedFreq2 = DoubleArray(observedFreq.size) { 0.0 }
                    for (i in expectedFreq2.indices) {
                        if (i != expectedFreq2.size - 1)
                            expectedFreq2[i] = run {
                                var expected = 0.0
                                for (j in bins[i]..<bins[i + 1]) {
                                    expected += lambda.pow(j) * exp(-1.0 * lambda) / factorial(j) * exponentialList.size
                                }
                                expected
                            }
                        else {
                            expectedFreq2[i] = run {
                                var expected = 0.0
                                for (j in bins[i]..poissonList.max()) {
                                    expected += lambda.pow(j) * exp(-1.0 * lambda) / factorial(j) * exponentialList.size
                                }
                                expected
                            }
                        }
                    }
                    var pAlpha = 0.0
                    for (i in 0 .. expectedFreq2.size - 1) {
                        Log.d(
                            TAG, "$i: ${
                                (pow(
                                    expectedFreq2[i] - observedFreq[i].toDouble(), 2.0
                                )) / expectedFreq2[i].toDouble()
                            }"
                        )
                        pAlpha += (pow(
                            expectedFreq2[i] - observedFreq[i].toDouble(), 2.0
                        )) / expectedFreq2[i].toDouble()
                    }
                    "bins: ${
                        bins.map {
                            String.format(
                                "%.2f",
                                it.toDouble()
                            )
                        }
                    } \n".plus("observedFreq: ${
                        observedFreq.map {
                            String.format(
                                "%.2f", it.toDouble()
                            )
                        }
                    } \n")
//                        .plus("expectedFreq (Library): ${
//                            expectedFreq.map {
//                                String.format(
//                                    "%.2f", it
//                                )
//                            }
//                        } \n")
                        .plus("expectedFreq: ${
                            expectedFreq2.map {
                                String.format(
                                    "%.2f", it
                                )
                            }
                        } \n")
                        .plus(
                            "pAlpha: $pAlpha \n"
                        )
                        .plus("alpha for n = ${k - 2}: 11.1\n")
                        .plus("is valid? ${if (pAlpha < 15.5) "H0 is valid" else "H0 is invalid"}")
                }, modifier = modifier
            )
        }
    }
}

private fun factorial(int: Int): Long {
    var fac = 1L
    var buf = 1
    while (buf < int) {
        fac *= buf + 1
        buf++
    }
    return fac
}